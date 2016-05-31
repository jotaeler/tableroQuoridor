/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juegoQuoridor.agentes;

import juegoQuoridor.utils.NumeroPartidasJugadas;
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
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.proto.SubscriptionResponder;
import jade.proto.SubscriptionResponder.Subscription;
import jade.proto.SubscriptionResponder.SubscriptionManager;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;
import jade.proto.ProposeInitiator;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedList;
import jade.content.onto.basic.Action;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Vector;
import java.util.ArrayList;
import java.util.Iterator;
import juegoQuoridor.GUI.GUI;
import juegoQuoridor.GUI.Ganador;
import juegoQuoridor.GUI.Quoridor;
import juegoQuoridor.GUI.Ranking;
import juegoQuoridor.elementos.FichaEntregada;
import juegoQuoridor.elementos.JugarPartida;
import juegoQuoridor.elementos.MovimientoRealizado;
import juegoQuoridor.utils.Casilla;
import juegoQuoridor.utils.JugadorRanking;
import juegoQuoridor.utils.NumeroPartidasGanadas;
import juegoQuoridor.utils.PartidaActiva;
import juegoQuoridor.utils.RepresentacionMovimiento;
import juegosTablero.elementos.Ficha;

import juegosTablero.elementos.Partida;
import juegosTablero.elementos.Tablero;
import juegosTablero.elementos.GanadorPartida;

import juegosTablero.elementos.Jugador;
import juegosTablero.elementos.Posicion;
import juegosTablero.elementos.ProponerPartida;

/**
 *
 * @author Jose Antonio Lopez
 */
public class AgenteTablero extends Agent {

    private Map<String, GUI> interfazTablero = new HashMap<String, GUI>();
    private Quoridor interfazInicio;
    private Ranking interfazRanking;
    private Ganador interfazGandor;

    /**
     * Estructura para guardar el ranking
     */
    Comparator<JugadorRanking> comparatorPG;
    PriorityQueue<JugadorRanking> partidasGanadas;

    private AID[] agentesJugador;
    //private LinkedList<AID> agentesJugador=new LinkedList<AID>();

    private ContentManager manager = (ContentManager) getContentManager();
    //El lenguaje utilizado por el agente para la comunicacíon es SL
    private Codec codec = new SLCodec();
    //La ontologia utilizada por el agente
    private Ontology ontology;
    private Map<String, ArrayList<Subscription>> suscripciones = new HashMap<String, ArrayList<Subscription>>();

    private Map<String, PartidaActiva> partidas = new HashMap<String, PartidaActiva>();

    int idPartidas = 0;

    private LinkedList<RepresentacionMovimiento> movimientosRealizados;

    private LinkedList<JugadorRanking> jugadorRanking;

    SubscriptionManager gestor;

    @Override
    protected void setup() {
        gestor = new SubscriptionManager() {

            public boolean register(Subscription suscripcion) {
                try {

                    suscripciones.get(((GanadorPartida) manager.extractContent(suscripcion.getMessage())).getPartida().getIdPartida()).add(suscripcion);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return true;
            }

            public boolean deregister(Subscription suscripcion) {
                try {
                    suscripciones.remove(((GanadorPartida) manager.extractContent(suscripcion.getMessage())).getPartida().getIdPartida());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        };

        movimientosRealizados = new LinkedList<RepresentacionMovimiento>();
        jugadorRanking = new LinkedList<JugadorRanking>();
        comparatorPG = new NumeroPartidasGanadas();
        partidasGanadas = new PriorityQueue<JugadorRanking>(comparatorPG);

        interfazInicio = new Quoridor(this);
        interfazInicio.setVisible(true);
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

        String idPartidaCN;

        public ProponerPartidaCN(Agent agente, ACLMessage plantilla, String _id) {
            super(agente, plantilla);
            idPartidaCN = _id;
        }

        //Método colectivo llamado tras finalizar el tiempo de espera o recibir todas las propuestas.
        protected void handleAllResponses(Vector respuestas, Vector responder) {
            // Evaluate proposals.
            AID[] jugadoresAID = new AID[20];
            ArrayList<Jugador> jugadores = new ArrayList<Jugador>();
            Comparator<JugadorRanking> comparator = new NumeroPartidasJugadas();
            PriorityQueue<JugadorRanking> listaJugadores = new PriorityQueue<JugadorRanking>(comparator);

            int nJugadoresDeseados = partidas.get(idPartidaCN).getPartida().getNumeroJugadores();

            int proposes = 0;
            Vector aceptados = new Vector();
            Enumeration e = respuestas.elements();
            int i = 0;
            while (e.hasMoreElements()) {
                ACLMessage msg = (ACLMessage) e.nextElement();

                if (msg.getPerformative() == ACLMessage.PROPOSE) {
                    ACLMessage reply = msg.createReply();
                    reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                    //Añado el jugador a la lista con prioridad
                    listaJugadores.add(new JugadorRanking(msg.getSender(), i));
                    aceptados.addElement(reply);
                    jugadoresAID[proposes] = msg.getSender();
                    proposes++;
                    i++;
                } else {
                    ACLMessage reply = msg.createReply();
                    reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
                    responder.addElement(reply);
                }
            }
            switch (nJugadoresDeseados) {
                case 2:
                    System.out.println("NUMERO DE LISTAJUGADORES: " + listaJugadores.size());
                    Iterator iteCase2 = listaJugadores.iterator();
                    int iCase2 = 0;
                    while (iteCase2.hasNext()) {
                        JugadorRanking jugadorR = listaJugadores.poll();
                        System.out.println("El jugador " + jugadorR.getJugador().getName() + " ha jugado "
                                + jugadorR.getPartidasJugadas() + " partidas");
                        int pos = jugadorR.getPosicionAceptados();
                        try {
                            if (iCase2 < 2) {
                                System.out.println("it vale: " + iCase2);
                                Ficha f = partidas.get(idPartidaCN).getSiguienteFicha();
                                manager.fillContent((ACLMessage) aceptados.get(pos), new FichaEntregada(f));
                                responder.add(aceptados.get(pos));
                                Jugador jugador = new Jugador(jugadoresAID[pos], f);
                                jugadores.add(jugador);
                                if (esta(jugador.getAgenteJugador()) != null) {
                                    incrementarPartida(jugador);
                                } else {
                                    jugadorRanking.addLast(new JugadorRanking(jugador.getAgenteJugador()));
                                }
                            } else {
                                /**
                                 * Por último rechazo a los jugador que tienen
                                 * más partidas jugadas
                                 */
                                ((ACLMessage) aceptados.get(pos)).setPerformative(ACLMessage.REJECT_PROPOSAL);
                                responder.add(aceptados.get(pos));
                            }
                        } catch (Exception err) {
                            err.printStackTrace();
                        }
                        iCase2++;
                    }

                    break;
                case 4:
                    System.out.println("NUMERO DE LISTAJUGADORES: " + listaJugadores.size());
                    Iterator iteCase4 = listaJugadores.iterator();
                    int iCase4 = 0;
                    while (iteCase4.hasNext()) {
                        JugadorRanking jugadorR = listaJugadores.poll();
                        int pos = jugadorR.getPosicionAceptados();
                        try {
                            if (iCase4 < 4) {
                                Ficha f = partidas.get(idPartidaCN).getSiguienteFicha();
                                manager.fillContent((ACLMessage) aceptados.get(pos), new FichaEntregada(f));
                                responder.add(aceptados.get(pos));
                                Jugador jugador = new Jugador(jugadoresAID[pos], f);
                                jugadores.add(jugador);
                                if (esta(jugador.getAgenteJugador()) != null) {
                                    incrementarPartida(jugador);
                                } else {
                                    jugadorRanking.addLast(new JugadorRanking(jugador.getAgenteJugador()));
                                }
                            } else {
                                ((ACLMessage) aceptados.get(pos)).setPerformative(ACLMessage.REJECT_PROPOSAL);
                                responder.add(aceptados.get(pos));
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        iCase4++;
                    }
                    
                    break;
            }
            partidas.get(idPartidaCN).setJugadores(jugadores);       
            jugarPartida(idPartidaCN);
        }

        //Manejador de los mensajes inform.
        protected void handleInform(ACLMessage inform) {

        }
    }

    /**
     * Clase para recoger los movimientos de los jugadores, tratarlos y enviarlos a todos los jugadores de la partida
     * hasta que un movimiento sea el ganador, en cuyo caso se notificará a todos los jugadores que la partida ha 
     * finalizado.
     */
    private class EnvioJugarPartida extends ProposeInitiator {

        String idPartidaPI;

        public EnvioJugarPartida(Agent agente, ACLMessage plantilla, String _id) {
            super(agente, plantilla);
            idPartidaPI = _id;
        }

        protected void handleAllResponses(Vector respuestas) {
            ACLMessage mensajeNuevo = null;
            ArrayList<Jugador> jugadores = new ArrayList<>();
            Enumeration e = respuestas.elements();
            while (e.hasMoreElements()) {
                ACLMessage msg = (ACLMessage) e.nextElement();
                if (msg.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
                    try {
                        MovimientoRealizado movimiento = (MovimientoRealizado) manager.extractContent(msg);
                        System.out.println("El AID del jugador es: " + msg.getSender());
                        int x = movimiento.getMovimiento().getPosicion().getCoorX();
                        int y = movimiento.getMovimiento().getPosicion().getCoorY();

                        //Comprobar Si ha ganado la partida --> Envio el inform suscribe
                        /**
                         * Si ha ganado la partida se incrementa en 1 el numero
                         * de partidas ganadas
                         */
                        JugadorRanking jugadorRa = estaPartidaGanadas(msg.getSender());
                        if (jugadorRa != null) {  //El jugador SI esta
                            //incremento en 1 su partida
                            jugadorRa.incrementarPartidaGanada();
                            System.out.println("SE HA INCREMENTADO LA PARTIDA GANADA");
                            System.out.println("EL JUGADOR EXISTE--> numero de jugadores: " + partidasGanadas.size());
                        } else {
                            JugadorRanking jr = new JugadorRanking(msg.getSender());
                            jr.incrementarPartidaGanada();
                            partidasGanadas.add(jr);
                            System.out.println("Se ha creado una nuevo jugadorRanking");
                            System.out.println("EL NUMERO DE OBJETOS DE PARTIDAS GANADAS ES: " + partidasGanadas.size());
                        }

                        Casilla c = new Casilla(x, y);
                        Casilla casilla = null;

                        casilla = partidas.get(idPartidaPI).getPosicionJugador(msg.getSender());
                        //Paso el movimiento al tablero
                        Posicion p = new Posicion(casilla.getX(), casilla.getY());

                        System.out.println("La posicion anterior es: " + p.getCoorX() + "," + p.getCoorY());
                        System.out.println("La nuevo posicion es: " + x + "," + y);
                        partidas.get(idPartidaPI).setPosicionJugador(msg.getSender(), x, y);

                        RepresentacionMovimiento rm = new RepresentacionMovimiento(movimiento, p);
                        movimientosRealizados.addLast(rm);

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
                        jugadores = partidas.get(idPartidaPI).getJugadores();
                        for (int i = 0; i < jugadores.size(); i++) {
                            mensajeNuevo.addReceiver(jugadores.get(i).getAgenteJugador());
                        }
                        Jugador jugadorActivo = partidas.get(idPartidaPI).getSiguienteTurno();
                        Partida partida = partidas.get(idPartidaPI).getPartida();
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

    private class HacerSuscripcion extends SubscriptionResponder {

        private Subscription suscripcion;

        public HacerSuscripcion(Agent agente, MessageTemplate plantilla, SubscriptionManager gestor) {
            super(agente, plantilla, gestor);
        }

        protected ACLMessage handleSubscription(ACLMessage propuesta)
                throws NotUnderstoodException {

            //Comprueba con extract content
            if (propuesta.getPerformative() == ACLMessage.SUBSCRIBE) {

                //Crea la suscripcion
                this.suscripcion = this.createSubscription(propuesta);

                try {
                    //El SubscriptionManager registra la suscripcion
                    if (gestor.register(suscripcion)) {
                        System.out.println("Jugador registrado");
                    }
                } catch (Exception e) {
                    System.out.println(": Error en el registro de la suscripción.");
                }

                //Acepta la propuesta y la envía
                ACLMessage agree = propuesta.createReply();
                agree.setPerformative(ACLMessage.AGREE);
                return agree;
            } else {
                //Rechaza la propuesta y la envía
                ACLMessage refuse = propuesta.createReply();
                refuse.setPerformative(ACLMessage.REFUSE);
                return refuse;
            }
        }

        protected ACLMessage handleCancel(ACLMessage cancelacion) {
            System.out.printf("Cancelacion");

            try {
                //El SubscriptionManager elimina del registro la suscripcion
                this.mySubscriptionManager.deregister(this.suscripcion);
            } catch (Exception e) {
                System.out.println(": Error en el desregistro de la suscripción.");
            }

            //Acepta la cancelación y responde
            ACLMessage cancela = cancelacion.createReply();
            cancela.setPerformative(ACLMessage.INFORM);
            return cancela;
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
     * Método que se ejecuta una sola vez al finalizar la clase ProponerPartidaCN, que envia el primer movimiento
     * a null y representa los movimientos en el tablero cada 2 segundos.
     * @param _id identificador de la partida
     */
    public void jugarPartida(String _id) {
        interfazTablero.put(_id, new GUI(manager));
        interfazTablero.get(_id).cargaFichas(partidas.get(_id).getPosJugadores());
        interfazTablero.get(_id).setVisible(true);
        ACLMessage mensaje = new ACLMessage(ACLMessage.PROPOSE);
        ArrayList<Jugador> jugadores;

        try {
            mensaje.setLanguage(codec.getName());
            mensaje.setOntology(ontology.getName());
            mensaje.setProtocol(FIPANames.InteractionProtocol.FIPA_PROPOSE);
            mensaje.setSender(getAID());
            mensaje.setReplyByDate(new Date(System.currentTimeMillis() + 2000));

            //Envio el mensaje a todos los jugadores
            jugadores = partidas.get(_id).getJugadores();
            for (int i = 0; i < jugadores.size(); i++) {
                mensaje.addReceiver(jugadores.get(i).getAgenteJugador());
            }

            Jugador jugadorActivo = partidas.get(_id).getSiguienteTurno();
            JugarPartida jugarpartida = new JugarPartida(partidas.get(_id).getPartida(), null, jugadorActivo);
            manager.fillContent(mensaje, new Action(getAID(), jugarpartida));
        } catch (Exception e) {
            e.printStackTrace();
        }

        addBehaviour(new EnvioJugarPartida(this, mensaje, _id));

        
        addBehaviour(new TickerBehaviour(this, 2000) {
            @Override
            protected void onTick() {
                //Elimina el movimiento de la lista
                if (movimientosRealizados.size() > 0) {
                    RepresentacionMovimiento m = movimientosRealizados.pop();
                    Casilla casilla = new Casilla(m.getPosAnterior().getCoorX(), m.getPosAnterior().getCoorY());
                    System.out.println("Estoy mandando al tablero la posicion: " + casilla.getX() + "," + casilla.getY());
                    interfazTablero.get(_id).representarMovimiento(m.getMr(), casilla);
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
            Partida p = new Partida(Integer.toString(idPartidas), juegoQuoridor.OntologiaQuoridor.TIPO_JUEGO, _nJugadores, t);
            PartidaActiva pa = new PartidaActiva(p);
            partidas.put(p.getIdPartida(), pa);
            ProponerPartida proponer = new ProponerPartida(p);
            Action ac = new Action(getAID(), proponer);
            try {
                manager.fillContent(mensajeCFP, ac);
            } catch (Exception e) {
                e.printStackTrace();
            }
            addBehaviour(new ProponerPartidaCN(this, mensajeCFP, Integer.toString(idPartidas)));
            idPartidas++;

        }
    }

    /**
     * Método parar enviar la estructura de datos que tiene el ranking de los
     * jugadores
     */
    
    public void enviarRanking() {
        interfazRanking = new Ranking();
        interfazRanking.recibirRanking(partidasGanadas);
        interfazRanking.representar();
        interfazRanking.setVisible(true);
    }
    
    /**
     * Método para mostrar la ventana de ganador
     */
    public void mostrarGanador() {
        interfazGandor = new Ganador();
        interfazGandor.setVisible(true);
    }

    /**
     * Método para ver si el jugador ya ha jugado antes la partida
     * @param j AID del jugador  
     * @return el jugador en caso que este en jugadorRanking, o null en caso contrario
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
     * Método para incrementar el número de partidas que ha jugado
     * @param j Jugador 
     */
    public void incrementarPartida(Jugador j) {
        for (JugadorRanking jugador : jugadorRanking) {
            if (jugador.getJugador().equals(j.getAgenteJugador())) {
                jugador.incrementarPartida();
                break;
            }
        }
    }

    /*Metodo para ver el ganador de la partida*/
    public void ComprobarGanarPartida(MovimientoRealizado movimiento, String idPartida) {
        if (partidas.get(idPartida).getJugadores().size() == 2) {
            if (movimiento.getJugador().getFicha().getColor() == juegoQuoridor.OntologiaQuoridor.COLOR_FICHA_1) {
                if (movimiento.getMovimiento().getPosicion().getCoorY() == 8) {
                    GanadorPartida(movimiento.getJugador(), partidas.get(idPartida).getPartida());
                }
            } else if (movimiento.getJugador().getFicha().getColor() == juegoQuoridor.OntologiaQuoridor.COLOR_FICHA_2) {
                if (movimiento.getMovimiento().getPosicion().getCoorY() == 0) {
                    GanadorPartida(movimiento.getJugador(), partidas.get(idPartida).getPartida());
                }
            }

        } else if (partidas.get(idPartida).getJugadores().size() == 4) {
            if (movimiento.getJugador().getFicha().getColor() == juegoQuoridor.OntologiaQuoridor.COLOR_FICHA_1) {
                if (movimiento.getMovimiento().getPosicion().getCoorY() == 8) {
                    GanadorPartida(movimiento.getJugador(), partidas.get(idPartida).getPartida());
                }
            } else if (movimiento.getJugador().getFicha().getColor() == juegoQuoridor.OntologiaQuoridor.COLOR_FICHA_2) {
                if (movimiento.getMovimiento().getPosicion().getCoorY() == 0) {
                    GanadorPartida(movimiento.getJugador(), partidas.get(idPartida).getPartida());
                }
            } else if (movimiento.getJugador().getFicha().getColor() == juegoQuoridor.OntologiaQuoridor.COLOR_FICHA_3) {
                if (movimiento.getMovimiento().getPosicion().getCoorX() == 8) {
                    GanadorPartida(movimiento.getJugador(), partidas.get(idPartida).getPartida());
                }
            } else if (movimiento.getJugador().getFicha().getColor() == juegoQuoridor.OntologiaQuoridor.COLOR_FICHA_4) {
                if (movimiento.getMovimiento().getPosicion().getCoorX() == 0) {
                    GanadorPartida(movimiento.getJugador(), partidas.get(idPartida).getPartida());
                }

            }

        }
    }

    public void GanadorPartida(Jugador j, Partida partida) {
        GanadorPartida ganador = new GanadorPartida(j, partida);
        ACLMessage mensaje = new ACLMessage(ACLMessage.INFORM);
        try {
            manager.fillContent(mensaje, ganador);
            Iterator<Subscription> Iterator = suscripciones.get(partida.getIdPartida()).iterator();
            if (suscripciones.get(partida.getIdPartida()).contains(partida.getIdPartida())) {
                for (int i = 0; i < suscripciones.get(partida.getIdPartida()).size(); i++) {
                    while (Iterator.hasNext()) {
                        Iterator.next().notify(mensaje);
                    }
                }
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
       
    }
    
    /**
     * Método para comprobar si un jugador esta en las partidasGanadas
     * @param j AID del jugador
     * @return el jugador en caso que este en partidasGanadas y null en caso contrario
     */
    public JugadorRanking estaPartidaGanadas(AID j) {
        for (JugadorRanking jugador : partidasGanadas) {
            if (jugador.getJugador().equals(j)) {
                return jugador;
            }
        }

        return null;

    }
}
