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
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetResponder;
import jade.proto.ProposeResponder;
import jade.proto.SubscriptionInitiator;
import java.util.ArrayList;
import juegoQuoridor.elementos.FichaEntregada;
import juegoQuoridor.elementos.JugarPartida;
import juegoQuoridor.elementos.Movimiento;
import juegoQuoridor.elementos.MovimientoRealizado;
import juegoQuoridor.utils.PartidaActiva;
import juegosTablero.elementos.Posicion;
import juegosTablero.elementos.ProponerPartida;

/**
 *
 * @author Jose Antonio Lopez
 */
public class AgenteJugador extends Agent {

    private AID[] agentesTablero;

    private ArrayList<String> mensajesPendientes;

    private ContentManager manager = (ContentManager) getContentManager();
    //El lenguaje utilizado por el agente para la comunicacíon es SL
    private Codec codec = new SLCodec();
    //La ontologia utilizada por el agente
    private Ontology ontology;

    private juegosTablero.elementos.Partida partida;
    private juegosTablero.elementos.Ficha ficha;

    @Override
    protected void setup() {
        //Inicialización de variables
        mensajesPendientes = new ArrayList();

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
            sd.setType(juegoQuoridor.OntologiaQuoridor.REGISTRO_JUGADOR);
            sd.setName("JALR0005");
            // Los agentes que quieran comunicarse deben conocer la ontolgía "Juego Quoridor"
            sd.addOntologies(juegoQuoridor.OntologiaQuoridor.ONTOLOGY_NAME);
            sd.addLanguages(FIPANames.ContentLanguage.FIPA_SL);
            dfd.addServices(sd);
            DFService.register(this, dfd);

        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        //Añadir las tareas principales
        //Creamos la plantilla a emplear, para solo recibir mensajes con el protocolo FIPA_PROPOSE y la performativa PROPOSE
        MessageTemplate templatePropose = ProposeResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_PROPOSE);
        MessageTemplate templateContractNet = ContractNetResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);

        // Se añaden las tareas principales
        this.addBehaviour(new RecibirTurno(this, templatePropose));
        this.addBehaviour(new ApuntarsePartida(this, templateContractNet));
    }

    /**
     * ******************************************************************
     *
     * Metodos de comunicación del agente
     *
     ********************************************************************
     */
    private class ApuntarsePartida extends ContractNetResponder {

        public ApuntarsePartida(Agent agente, MessageTemplate plantilla) {
            super(agente, plantilla);
        }

        // Recibo ProponerPartida
        protected ACLMessage prepareResponse(ACLMessage proponerPartida) throws NotUnderstoodException, RefuseException {
            //Compruebo si ya tengo una partida
            if (partida == null) {
                //Como no hay partida iniciada, cojo los datos de la partida que me pasa el tablero
                ACLMessage respuesta = proponerPartida.createReply();
                try {

                    Action ac = (Action) manager.extractContent(proponerPartida);
                    ProponerPartida pp = (ProponerPartida) ac.getAction();
                    if (pp.getPartida().getTipoJuego().equals(juegoQuoridor.OntologiaQuoridor.TIPO_JUEGO)) {
                        partida = pp.getPartida();
                        respuesta.setPerformative(ACLMessage.PROPOSE);
                        System.out.println("Aceptada partida");
                    } else {
                        //No juego a ese tipo de juego
                        System.out.println("No juego a ese tipo de juego " + pp.getPartida().getTipoJuego());
                        respuesta.setPerformative(ACLMessage.REFUSE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return respuesta;
            } else {
                //Ya tengo una partida
                throw new RefuseException("");
            }

        }

        //Recibo la ficha
        protected ACLMessage prepareResultNotification(ACLMessage cfp, ACLMessage propose, ACLMessage accept) {

            ACLMessage respuesta = accept.createReply();
            try {

                respuesta.setPerformative(ACLMessage.INFORM);
                FichaEntregada f = (FichaEntregada) manager.extractContent(accept);
                ficha = f.getFicha();
                System.out.println("Se me asigna la ficha " + ficha.getColor());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return respuesta;
        }

        protected void handleRejectProposal(ACLMessage cfp, ACLMessage propose, ACLMessage reject) {
            //Me han rechazado la partida
            System.out.println("Se me ha rechazado en la partida");
            partida = null;
        }

    }

    /**
     * Recibe el turno, realiza movimiento y envía el movimiento
     */
    private class RecibirTurno extends ProposeResponder {

        public RecibirTurno(Agent _a, MessageTemplate _m) {
            super(_a, _m);
        }

        protected ACLMessage prepareResponse(ACLMessage jugarPartida) throws NotUnderstoodException {

            ACLMessage respuesta = jugarPartida.createReply();

            try {
                Action ac = (Action) manager.extractContent(jugarPartida);
                JugarPartida jp = (JugarPartida) ac.getAction();
                if (jp.getJugadorActivo().getAgenteJugador().equals(getAID())) {
                    respuesta.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                    /*
                    Extraer todo el conocimiento del objeto movimientoAnterior
                     */
                    manager.fillContent(respuesta, mover(jp));
                    System.out.println("Movimiento enviado por agente "+getAID().getName());
                } else {
                    System.out.println("No es mi turno "+getAID().getName());
                    respuesta.setPerformative(ACLMessage.REJECT_PROPOSAL);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return respuesta;

        }

    }

    private class GanadorPartida extends SubscriptionInitiator {

        private int suscripciones = 0;

        public GanadorPartida(Agent agente, ACLMessage mensaje) {
            super(agente, mensaje);
        }

        //Maneja la respuesta en caso que acepte: AGREE
        protected void handleAgree(ACLMessage inform) {

        }

        // Maneja la respuesta en caso que rechace: REFUSE
        protected void handleRefuse(ACLMessage inform) {

        }

        //Maneja la informacion enviada: INFORM
        protected void handleInform(ACLMessage inform) {

            //Se informa del ganador de la partida
            try {
                GanadorPartida ganador = (GanadorPartida) manager.extractContent(inform);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        //Maneja la respuesta en caso de fallo: FAILURE
        protected void handleFailure(ACLMessage failure) {
            //Se comprueba si el fallo viene del AMS o de otro agente.
//            if (failure.getSender().equals(myAgent.getAMS())) {
//                System.out.println(SubscriptionIni.this.getLocalName() + ": El destinatario no existe.");
//            } else {
//                System.out.printf("%s: El agente %s falló al intentar realizar la acción solicitada.\n",
//                    SubscriptionIni.this.getLocalName(), failure.getSender().getName());
//            }
        }
    }

    /**
     * ******************************************************************
     *
     * Metodos de juego del agente
     *
     ********************************************************************
     */
    //Realiza un movimiento
    //Hay que determinar que parametros son necesarios pasar a este metodo
    private MovimientoRealizado mover(JugarPartida _jp) {
        int x=(int)Math.random()*8;
        int y=(int)Math.random()*8;
        Posicion p=new Posicion(x,y);
        Movimiento m=new Movimiento(ficha, p);
       
        return new MovimientoRealizado(_jp.getJugadorActivo(), m);
    }

}
