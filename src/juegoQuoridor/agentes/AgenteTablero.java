/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juegoQuoridor.agentes;

import jade.content.ContentManager;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.BeanOntologyException;
import jade.content.onto.Ontology;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;
import jade.proto.ProposeInitiator;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedList;
import jade.content.onto.basic.Action;
import java.util.Vector;
import juegoQuoridor.GUI.GUI;
import juegoQuoridor.GUI.Quoridor;
import juegoQuoridor.elementos.FichaEntregada;
import juegoQuoridor.elementos.JugarPartida;
import juegoQuoridor.elementos.MovimientoRealizado;
import juegoQuoridor.utils.Casilla;
import juegoQuoridor.utils.JugadorRanking;
import juegoQuoridor.utils.PartidaActiva;
import juegoQuoridor.utils.RepresentacionMovimiento;
import juegosTablero.elementos.Ficha;

import juegosTablero.elementos.Partida;
import juegosTablero.elementos.Tablero;

import juegosTablero.elementos.Jugador;
import juegosTablero.elementos.Posicion;
import juegosTablero.elementos.ProponerPartida;

/**
 *
 * @author Jose Antonio Lopez
 */
public class AgenteTablero extends Agent {

    private GUI interfazTablero;
    private Quoridor interfazInicio;

    private AID[] agentesJugador;
    //private LinkedList<AID> agentesJugador=new LinkedList<AID>();

    private ContentManager manager = (ContentManager) getContentManager();
    //El lenguaje utilizado por el agente para la comunicacíon es SL
    private Codec codec = new SLCodec();
    //La ontologia utilizada por el agente
    private Ontology ontology;

    private Casilla[][] tablero = new Casilla[9][9];
    private PartidaActiva partidaActual = null;

    private LinkedList<RepresentacionMovimiento> movimientosRealizados;
    private LinkedList<Casilla> movimientosAnteriores;
    private LinkedList<JugadorRanking> jugadorRanking;
    //por número de partidas jugadas.
    private Vector<PairJugadorPosicion> jugar;   //Vector para saber que jugadores son lo elegidos, por el número de partidas

    @Override
    protected void setup() {
        //Creo la matriz de casillas del tablero
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                tablero[i][j] = new Casilla(i, j);
            }
        }
        movimientosRealizados = new LinkedList<RepresentacionMovimiento>();
        movimientosAnteriores = new LinkedList<Casilla>();
        jugadorRanking = new LinkedList<JugadorRanking>();
        jugar = new Vector();
        interfazInicio = new Quoridor(this);
        interfazInicio.setVisible(true);
        //Inicialización de variables

        try {
            ontology = juegoQuoridor.OntologiaQuoridor.getInstance();
        } catch (BeanOntologyException ex) {
            ex.printStackTrace();
        }

        manager.registerLanguage(codec);
        manager.registerOntology(ontology);

        try {
            //Registro en páginas Amarrillas
            DFAgentDescription dfd = new DFAgentDescription();
            dfd.setName(getAID());
            ServiceDescription sd = new ServiceDescription();
            sd.setType(juegoQuoridor.OntologiaQuoridor.REGISTRO_TABLERO);
            sd.setName("The Fellowship of the Agent Tablero");
            // Los agentes que quieran comunicarse deben conocer la ontolgía "Juego Quoridor"
            sd.addOntologies(juegoQuoridor.OntologiaQuoridor.ONTOLOGY_NAME);
            sd.addLanguages(FIPANames.ContentLanguage.FIPA_SL);
            dfd.addServices(sd);
            DFService.register(this, dfd);
            sd.addLanguages(FIPANames.ContentLanguage.FIPA_SL);

            System.out.println("registrado en la plataforma");

        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        //Añadir las tareas principales
        addBehaviour(new BuscarAgentes(this, 15000));

        //Creamos la plantilla a emplear, para solo recibir mensajes con el protocolo FIPA_PROPOSE y la performativa PROPOSE
        // MessageTemplate templatePropose = ProposeResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_PROPOSE);
        // Se añaden las tareas principales
        //   this.addBehaviour(new RecibirTurno(this, templatePropose));
    }

    /**
     * ******************************************************************
     *
     * Metodos de comunicación del agente
     *
     ********************************************************************
     */
    public class BuscarAgentes extends TickerBehaviour {

        //Se buscarán agentes consola y operación
        public BuscarAgentes(Agent a, long period) {
            super(a, period);
        }

        @Override
        protected void onTick() {
            DFAgentDescription template;
            ServiceDescription sd;
            DFAgentDescription[] result;

            //Buscar agentes jugadores
            template = new DFAgentDescription();
            sd = new ServiceDescription();
            sd.setType(juegoQuoridor.OntologiaQuoridor.REGISTRO_JUGADOR);
            template.addServices(sd);

            try {
                System.out.println("buscando agentes...");
                result = DFService.search(myAgent, template);
                if (result.length > 0) {
                    agentesJugador = new AID[result.length];
                    for (int i = 0; i < result.length; ++i) {
                        agentesJugador[i] = result[i].getName();
                        System.out.println("Registrado nuevo agente: " + result[i].getName());
                    }

                } else {
                    //No se han encontrado agentes jugador
                    agentesJugador = null;
                }
            } catch (FIPAException fe) {
                fe.printStackTrace();
            }
        }
    }

    private class ProponerPartidaCN extends ContractNetInitiator {

        public ProponerPartidaCN(Agent agente, ACLMessage plantilla) {
            super(agente, plantilla);
        }

        //Método colectivo llamado tras finalizar el tiempo de espera o recibir todas las propuestas.
        protected void handleAllResponses(Vector respuestas, Vector responder) {
            // Evaluate proposals.
            AID[] jugadoresAID = new AID[4];
            ArrayList<Jugador> jugadores = new ArrayList<Jugador>();
            int nJugadoresDeseados = partidaActual.getPartida().getNumeroJugadores();
            int proposes = 0;
            int numPartidas = 0;
            Vector aceptados = new Vector();
            Enumeration e = respuestas.elements();
            while (e.hasMoreElements()) {
                ACLMessage msg = (ACLMessage) e.nextElement();

                if (msg.getPerformative() == ACLMessage.PROPOSE && proposes <= nJugadoresDeseados) {

                    ACLMessage reply = msg.createReply();
                    reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                    aceptados.addElement(reply);
                    jugadoresAID[proposes] = msg.getSender();
                    proposes++;
                } else {
                    ACLMessage reply = msg.createReply();
                    reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
                    responder.addElement(reply);
                }

            }
            switch (proposes) {
                case 0:
                    //RELLENAR
                    break;
                case 1:
                    ((ACLMessage) aceptados.get(0)).setPerformative(ACLMessage.REJECT_PROPOSAL);
                    responder.add(aceptados.get(0));
                    break;
                case 2:
                    for (int i = 0; i < 2; i++) {
                        try {
                            Ficha f = partidaActual.getSiguienteFicha();
                            manager.fillContent((ACLMessage) aceptados.get(i), new FichaEntregada(f));
                            responder.add(aceptados.get(i));
                            Jugador jugador = new Jugador(jugadoresAID[i], f);
                            jugadores.add(jugador);
                            if (esta(jugador.getAgenteJugador()) != null) {
                                incrementarPartida(jugador);
                            } else {
                                jugadorRanking.addLast(new JugadorRanking(jugador.getAgenteJugador()));
                            }
                        } catch (Exception err) {
                            err.printStackTrace();
                        }
                    }
                    partidaActual.setJugadores(jugadores);
                    jugarPartida();
                    break;
                case 3:
                    for (int i = 0; i < 3; i++) {
                        try {
                            JugadorRanking jr = esta(jugadoresAID[i]);
                            if (jr != null) {  //El jugador ha jugado alguna partida
                                if (i < 2) {
                                    jugar.add(new PairJugadorPosicion(jr, i));
                                } else {    //Es el tercer jugador          
                                    numPartidas = jr.getPartidasJugadas();
                                    //comprubro cual es el JugadorRanking que menos partida tiene
                                    JugadorRanking jrMenor = jugadorMenosPartidas();
                                    //Si numPartidas es menor que el jrMenor, elimino a jrMenor del vector e inserto al jr
                                    if (numPartidas < jrMenor.getPartidasJugadas()) {
                                        jugar.remove(jrMenor);
                                        jugar.add(new PairJugadorPosicion(jr, i));
                                    }
                                }
                            } else if (i == 2) {
                                numPartidas = jr.getPartidasJugadas();
                                JugadorRanking jrMenor = jugadorMenosPartidas();
                                //Si numPartidas es menor que el jrMenor, elimino a jrMenor del vector e inserto al jr
                                if (numPartidas < jrMenor.getPartidasJugadas()) {
                                    jugar.remove(jrMenor);
                                    jugar.add(new PairJugadorPosicion(jr, i));
                                    jugadorRanking.addLast(new JugadorRanking(jugadoresAID[i]));
                                }
                            } else {
                                jugadorRanking.addLast(new JugadorRanking(jugadoresAID[i]));
                                jugar.add(new PairJugadorPosicion(jr, i));
                            }
                        } catch (Exception err) {
                            err.printStackTrace();
                        }
                    }

                    /**
                     * *
                     * Recorro el vector de jugar que es donde estan los
                     * jugadores que son aceptados en la partida el vector jugar
                     * tiene un tipo PairJugadorPosicion que tiene un
                     * JugadorRanking, junto con la posición que se corresponde
                     * con el vector de aceptados
                     */
                    for (int i = 0; i < jugar.size(); i++) {
                        try {
                            int posicion = jugar.get(i).getPosicion();
                            Ficha f = partidaActual.getSiguienteFicha();
                            manager.fillContent((ACLMessage) aceptados.get(posicion), new FichaEntregada(f));
                            responder.add(aceptados.get(posicion));
                            Jugador jugador = new Jugador(jugadoresAID[posicion], f);
                            jugadores.add(jugador);
                            incrementarPartida(jugador);
                        } catch (Exception err) {
                            err.printStackTrace();
                        }
                    }

                    /**
                     * Por último rechazo al jugador que tiene más partidas jugadas
                     */
                    int posicionRechazo = jugadorRechazado();
                    ((ACLMessage) aceptados.get(posicionRechazo)).setPerformative(ACLMessage.REJECT_PROPOSAL);
                    responder.add(aceptados.get(posicionRechazo));
                    partidaActual.setJugadores(jugadores);
                    jugarPartida();
                    break;
                case 4:
                    for (int i = 0; i < 4; i++) {
                        try {
                            Ficha f = partidaActual.getSiguienteFicha();
                            manager.fillContent((ACLMessage) aceptados.get(i), new FichaEntregada(f));
                            responder.add(aceptados.get(i));
                            Jugador jugador = new Jugador(jugadoresAID[i], f);
                            jugadores.add(jugador);
                            if (esta(jugador.getAgenteJugador()) != null) {
                                incrementarPartida(jugador);
                            } else {
                                jugadorRanking.addLast(new JugadorRanking(jugador.getAgenteJugador()));
                            }
                        } catch (Exception err) {
                            err.printStackTrace();
                        }
                    }
                    partidaActual.setJugadores(jugadores);
                    jugarPartida();
                    break;
            }
        }

        //Manejador de los mensajes inform.
        protected void handleInform(ACLMessage inform) {

        }
    }

    private class EnvioJugarPartida extends ProposeInitiator {

        public EnvioJugarPartida(Agent agente, ACLMessage plantilla) {
            super(agente, plantilla);
        }

        protected void handleAllResponses(Vector respuestas) {
            ACLMessage mensajeNuevo = null;
            ArrayList<Jugador> jugadores = new ArrayList<>();
            System.out.println("HandleAllResponses EnvioJugarPartida");
            Enumeration e = respuestas.elements();
            while (e.hasMoreElements()) {
                System.out.println("Agente tablero - Dentro de e.hasMoreElements");
                ACLMessage msg = (ACLMessage) e.nextElement();
                if (msg.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
                    System.out.println("Agente tablero - He recibido un accept proposal");

                    //Comprobar Si ha ganado la partida --> Envio el inform suscribe
                    try {
                        MovimientoRealizado movimiento = (MovimientoRealizado) manager.extractContent(msg);
                        System.out.println("El AID del jugador es: " + msg.getSender());
                        int x = movimiento.getMovimiento().getPosicion().getCoorX();
                        int y = movimiento.getMovimiento().getPosicion().getCoorY();

                        Casilla c = new Casilla(x, y);
                        Casilla casilla = null;

                        if (movimientosAnteriores.size() > 1) {
                            System.out.println("DENTRO DEL ARRAY MOVIMIENTOS ANTERIORES");
                            casilla = movimientosAnteriores.pop();
                        } else {
                            casilla = partidaActual.getPosicionJugador(msg.getSender());
                        }

                        //Paso el movimiento al tablero
                        Posicion p = new Posicion(casilla.getX(), casilla.getY());

                        System.out.println("La posicion anterior es: " + p.getCoorX() + "," + p.getCoorY());
                        System.out.println("La nuevo posicion es: " + x + "," + y);

                        RepresentacionMovimiento rm = new RepresentacionMovimiento(movimiento, p);
                        movimientosRealizados.addLast(rm);

                        movimientosAnteriores.addLast(c);
                        System.out.println("Se ha añadido un nuevo movimiento a la partida activa");

                        System.out.println("Agente tablero ha recibido un movmiento a " + movimiento.toString());

                        //Reinicio el comportamiento
                        //Nuevo mensaje con el movimiento realizado
                        mensajeNuevo = new ACLMessage(ACLMessage.PROPOSE);
                        mensajeNuevo.setLanguage(codec.getName());
                        mensajeNuevo.setOntology(ontology.getName());
                        mensajeNuevo.setProtocol(FIPANames.InteractionProtocol.FIPA_PROPOSE);
                        mensajeNuevo.setSender(getAID());

                        //Envio el mensaje a todos los jugadores
                        jugadores = partidaActual.getJugadores();
                        for (int i = 0; i < jugadores.size(); i++) {
                            mensajeNuevo.addReceiver(jugadores.get(i).getAgenteJugador());
                        }
                        Jugador jugadorActivo = partidaActual.getSiguienteTurno();
                        Partida partida = partidaActual.getPartida();
                        JugarPartida jugarpartida = new JugarPartida(partida, movimiento.getMovimiento(), jugadorActivo);
                        manager.fillContent(mensajeNuevo, new Action(getAID(), jugarpartida));
                        reset(mensajeNuevo);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * ******************************************************************
     *
     * Metodos de juego del agente
     *
     ********************************************************************
     */
    /**
     * Método jugar partida
     */
    public void jugarPartida() {
        interfazTablero = new GUI(manager);
        interfazTablero.cargaFichas(partidaActual.getPosJugador());
        interfazTablero.setVisible(true);
        ACLMessage mensaje = new ACLMessage(ACLMessage.PROPOSE);
        ArrayList<Jugador> jugadores;

        try {
            mensaje.setLanguage(codec.getName());
            mensaje.setOntology(ontology.getName());
            mensaje.setProtocol(FIPANames.InteractionProtocol.FIPA_PROPOSE);
            mensaje.setSender(getAID());
            mensaje.setReplyByDate(new Date(System.currentTimeMillis() + 2000));

            //Envio el mensaje a todos los jugadores
            jugadores = partidaActual.getJugadores();
            for (int i = 0; i < jugadores.size(); i++) {
                mensaje.addReceiver(jugadores.get(i).getAgenteJugador());
            }

            Jugador jugadorActivo = partidaActual.getSiguienteTurno();
            JugarPartida jugarpartida = new JugarPartida(partidaActual.getPartida(), null, jugadorActivo);
            manager.fillContent(mensaje, new Action(getAID(), jugarpartida));
        } catch (Exception e) {
            e.printStackTrace();
        }

        addBehaviour(new EnvioJugarPartida(this, mensaje));

        addBehaviour(new TickerBehaviour(this, 2000) {
            @Override
            protected void onTick() {
                //Elimina el movimiento de la lista
                if (movimientosRealizados.size() > 0) {
                    RepresentacionMovimiento m = movimientosRealizados.pop();
                    Casilla casilla = new Casilla(m.getPosAnterior().getCoorX(), m.getPosAnterior().getCoorY());
                    System.out.println("Estoy mandando al tablero la posicion: " + casilla.getX() + "," + casilla.getY());
                    interfazTablero.representarMovimiento(m.getMr(), casilla);
                }
            }
        });
    }

    /**
     * Este método es invocado por la interfaz Quoridor. Las tareas de este
     * agente serán: 1- Comprobar si hay agentes Jugador en la estructura
     * correspondiente 2- Crear el contractNet ProponerPartida a los agentes
     * jugador
     */
    public void empezarPartida(int _nJugadores) {

        ACLMessage mensajeCFP = new ACLMessage(ACLMessage.CFP);
        mensajeCFP.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
        mensajeCFP.setLanguage(codec.getName());
        mensajeCFP.setOntology(ontology.getName());
        mensajeCFP.setSender(getAID());
        //Time Out 1 seg
        mensajeCFP.setReplyByDate(new Date(System.currentTimeMillis() + 1000));

        if (agentesJugador.length >= _nJugadores) {
            for (int i = 0; i < _nJugadores; i++) {
                mensajeCFP.addReceiver(agentesJugador[i]);
            }
            Tablero t = new Tablero(9, 9);
            Partida p = new Partida("1", juegoQuoridor.OntologiaQuoridor.TIPO_JUEGO, _nJugadores, t);
            partidaActual = new PartidaActiva(p);
            ProponerPartida proponer = new ProponerPartida(p);
            Action ac = new Action(getAID(), proponer);
            try {
                manager.fillContent(mensajeCFP, ac);
            } catch (Exception e) {
                e.printStackTrace();
            }
            addBehaviour(new ProponerPartidaCN(this, mensajeCFP));

        }
    }

    /**
     * Método para ver si el jugador ya ha jugado antes la partida
     */
    public JugadorRanking esta(AID j) {
        for (JugadorRanking jugador : jugadorRanking) {
            if (jugador.getJugador().equals(j)) {
                return jugador;
            }
        }
        return null;
    }

    /**
     * Método para incrementar el numero de partidas que ha jugado
     */
    public void incrementarPartida(Jugador j) {
        for (JugadorRanking jugador : jugadorRanking) {
            if (jugador.getJugador().equals(j.getAgenteJugador())) {
                jugador.incrementarPartida();
                break;
            }
        }
    }

    /**
     * *
     * Método para saber cual es el jugador que menos partidas tiene
     */
    public JugadorRanking jugadorMenosPartidas() {
        int i = 0;
        if (jugar.get(i).getJugadorR().getPartidasJugadas() < jugar.get(i + 1).getJugadorR().getPartidasJugadas()) {
            return jugar.get(i).getJugadorR();
        }
        return jugar.get(i + 1).getJugadorR();
    }

    public int jugadorRechazado() {
        int i = 0;
        int pos = -1;
        if (jugar.get(i).getPosicion() != 0 && jugar.get(i + 1).getPosicion() != 0) {
            pos = 0;
        }
        if (jugar.get(i).getPosicion() != 1 && jugar.get(i + 1).getPosicion() != 1) {
            pos = 1;
        }
        if (jugar.get(i).getPosicion() != 2 && jugar.get(i + 1).getPosicion() != 2) {
            pos = 2;
        }
        return pos;
    }

}
