/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juegoQuoridor.agentes;

import juegoQuoridor.GUI.GUItablero;
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
import jade.proto.ProposeInitiator;
import java.util.ArrayList;
import ontologiaConsola.MensajeEnConsola;

/**
 *
 * @author Jose Antonio Lopez
 */
public class AgenteTablero extends Agent {

    private GUItablero interfazTablero;

    private AID[] agentesConsola;
    private AID[] agentesJugador;

    private ArrayList<String> mensajesPendientes;

    private ContentManager manager = (ContentManager) getContentManager();
    //El lenguaje utilizado por el agente para la comunicacíon es SL
    private Codec codec = new SLCodec();
    //La ontologia utilizada por el agente
    private Ontology ontology;

    @Override
    protected void setup() {
        interfazTablero = new GUItablero(this);
        interfazTablero.setVisible(true);
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
            sd.setType(juegoQuoridor.OntologiaQuoridor.REGISTRO_TABLERO);
            sd.setName("The Fellowship of the Agent");
            dfd.addServices(sd);
            DFService.register(this, dfd);

            // Los agentes que quieran comunicarse deben conocer la ontolgía "Juego Quoridor"
            sd.addOntologies(juegoQuoridor.OntologiaQuoridor.ONTOLOGY_NAME);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        //Añadir las tareas principales
        addBehaviour(new BuscarAgentes(this, 5000));
        addBehaviour(new EnvioConsolaTicker(this, 10000));

        //Creamos la plantilla a emplear, para solo recibir mensajes con el protocolo FIPA_PROPOSE y la performativa PROPOSE
        // MessageTemplate templatePropose = ProposeResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_PROPOSE);
        // MessageTemplate templateContractNet = ContractNetResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
        // Se añaden las tareas principales
        //   this.addBehaviour(new RecibirTurno(this, templatePropose));
        //  this.addBehaviour(new ApuntarsePartida(this, templateContractNet));
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

            //Busca agentes consola
            template = new DFAgentDescription();
            sd = new ServiceDescription();
            sd.setName(ontologiaConsola.OntologiaPrimeraPractica.AGENTE_CONSOLA);
            template.addServices(sd);

            try {
                result = DFService.search(myAgent, template);
                if (result.length > 0) {
                    agentesConsola = new AID[result.length];
                    for (int i = 0; i < result.length; ++i) {
                        agentesConsola[i] = result[i].getName();
                    }
                } else {
                    //No se han encontrado agentes consola
                    agentesConsola = null;
                }
            } catch (FIPAException fe) {
                fe.printStackTrace();
            }

            //Buscar agentes jugadores
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
     * ******************************************************************
     *
     * Metodos para mostrar mensajes en la consola
     *
     ********************************************************************
     */
    private class EnvioConsola extends ProposeInitiator {

        public EnvioConsola(Agent agente, ACLMessage mensaje) {
            super(agente, mensaje);
        }

        //Manejar la respuesta en caso que acepte: ACCEPT_PROPOSAL
        protected void handleAcceptProposal(ACLMessage aceptacion) {

        }

        //Manejar la respuesta en caso que rechace: REJECT_PROPOSAL
        protected void handleRejectProposal(ACLMessage rechazo) {

        }
    }

    public class EnvioConsolaTicker extends TickerBehaviour {

        public EnvioConsolaTicker(Agent a, long period) {
            super(a, period);
        }

        @Override
        protected void onTick() {
            ACLMessage mensaje;
            if (agentesConsola != null) {
                if (!mensajesPendientes.isEmpty()) {
                    try {
                        mensaje = new ACLMessage(ACLMessage.PROPOSE);
                        mensaje.setLanguage(codec.getName());
                        mensaje.setOntology(ontology.getName());
                        mensaje.setProtocol(FIPANames.InteractionProtocol.FIPA_PROPOSE);
                        mensaje.setSender(myAgent.getAID());
                        mensaje.addReceiver(agentesConsola[0]);
                        MensajeEnConsola m = new MensajeEnConsola(myAgent.getAID().getName(), mensajesPendientes.remove(0));
                        manager.fillContent(mensaje, m);
                        addBehaviour(new EnvioConsola(myAgent, mensaje));
                        //myAgent.send(mensaje);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    //Si queremos hacer algo si no tenemos mensajes
                    //pendientes para enviar a la consola
                }
            }
        }
    }

}
