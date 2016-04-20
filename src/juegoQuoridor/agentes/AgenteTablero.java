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
import juegoQuoridor.utils.PartidaActiva;
import juegosTablero.elementos.Ficha;

import juegosTablero.elementos.Partida;
import juegosTablero.elementos.Tablero;

import juegosTablero.elementos.Jugador;
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

    private LinkedList<MovimientoRealizado> movimientosRealizados;

    @Override
    protected void setup() {
        //Creo la matriz de casillas del tablero
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                tablero[i][j] = new Casilla(i, j);
            }
        }
        movimientosRealizados = new LinkedList<MovimientoRealizado>();
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
            Vector aceptados = new Vector();
            ACLMessage accept = null;
            Enumeration e = respuestas.elements();
            while (e.hasMoreElements()) {
                ACLMessage msg = (ACLMessage) e.nextElement();

                if (msg.getPerformative() == ACLMessage.PROPOSE && proposes < nJugadoresDeseados) {
                    proposes++;

                    ACLMessage reply = msg.createReply();
                    reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                    aceptados.addElement(reply);
                    jugadoresAID[proposes] = msg.getSender();
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
                            jugadores.add(new Jugador(jugadoresAID[i], f));
                        } catch (Exception err) {
                            err.printStackTrace();
                        }
                    }
                    partidaActual.setJugadores(jugadores);
                    jugarPartida();
                    break;
                case 3:
                    for (int i = 0; i < 2; i++) {
                        try {
                            Ficha f = partidaActual.getSiguienteFicha();
                            manager.fillContent((ACLMessage) aceptados.get(i), new FichaEntregada(f));
                            responder.add(aceptados.get(i));
                            jugadores.add(new Jugador(jugadoresAID[i], f));
                        } catch (Exception err) {
                            err.printStackTrace();
                        }
                    }
                    ((ACLMessage) aceptados.get(2)).setPerformative(ACLMessage.REJECT_PROPOSAL);
                    responder.add(aceptados.get(2));
                    partidaActual.setJugadores(jugadores);
                    jugarPartida();
                    break;
                case 4:
                    for (int i = 0; i < 4; i++) {
                        try {
                            Ficha f = partidaActual.getSiguienteFicha();
                            manager.fillContent((ACLMessage) aceptados.get(i), new FichaEntregada(f));
                            responder.add(aceptados.get(i));
                            jugadores.add(new Jugador(jugadoresAID[i], f));
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

        protected void handleAcceptProposal(ACLMessage aceptacion) {

        }

        protected void handleRejectProposal(ACLMessage rechazo) {

        }

        protected void handleAllResponse(Vector respuestas, Vector aceptados) {
            int jugadoresAc = 0;
            ACLMessage mensajeNuevo = null;
            ArrayList<Jugador> jugadores = new ArrayList<>();

            Enumeration e = respuestas.elements();
            while (e.hasMoreElements()) {
                System.out.println("Agente tablero - Dentro de e.hasMoreElements");
                ACLMessage msg = (ACLMessage) e.nextElement();
                ACLMessage reply = msg.createReply();
                if (msg.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
                    System.out.println("Agente tablero - He recibido un accept proposal");
                    aceptados.add(reply);

                    //Comprobar Si ha ganado la partida --> Envio el inform suscribe
                    try {

                        MovimientoRealizado movimiento = (MovimientoRealizado) manager.extractContent(msg);

                        //Paso el movimiento al tablero
                        movimientosRealizados.addLast(movimiento);
                        System.out.println("Agente tablero ha recibido un movmiento a "+movimiento.toString());

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
     * /*
     * Método jugar partida
     */
    public void jugarPartida() {
        interfazTablero = new GUI(manager);
        interfazTablero.cargaFichas(partidaActual.getPosJugador());
        interfazTablero.setVisible(true);
        ACLMessage mensaje = null;
        ArrayList<Jugador> jugadores;

        try {
            mensaje = new ACLMessage(ACLMessage.PROPOSE);
            mensaje.setLanguage(codec.getName());
            mensaje.setOntology(ontology.getName());
            mensaje.setProtocol(FIPANames.InteractionProtocol.FIPA_PROPOSE);
            mensaje.setSender(this.getAID());

            //Envio el mensaje a todos los jugadores
            jugadores = partidaActual.getJugadores();
            System.out.println("Agente tablero jugarPartida - N jugadores="+jugadores.size());
            for (int i = 0; i < jugadores.size(); i++) {
                mensaje.addReceiver(jugadores.get(i).getAgenteJugador());
            }
//                  Jugador jugadorActivo = partidaActual.getPosicionJugador(agentesJugador[0]).getJugador();
            Jugador jugadorActivo = partidaActual.getSiguienteTurno();
            JugarPartida jugarpartida = new JugarPartida(partidaActual.getPartida(), null, jugadorActivo);
            manager.fillContent(mensaje, new Action(getAID(), jugarpartida));
        } catch (Exception e) {
            e.printStackTrace();
        }

        addBehaviour(new EnvioJugarPartida(this, mensaje));

        addBehaviour(new TickerBehaviour(this, 3000) {
            @Override
            protected void onTick() {
                //Elimina el movimiento de la lista
                if (movimientosRealizados.size() > 0) {
                    MovimientoRealizado m = movimientosRealizados.pop();
                    interfazTablero.representarMovimiento(m, partidaActual.getPosicionJugador(m.getJugador().getAgenteJugador()));
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

}
