/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juegoQuoridor.agentes;

import juegoQuoridor.GUI.Consola;
import jade.content.ContentManager;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.BeanOntologyException;
import jade.content.onto.Ontology;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ProposeResponder;
import java.util.ArrayList;
import java.util.Iterator;
import ontologiaConsola.MensajeEnConsola;
import ontologiaConsola.MensajeConsola;

/**
 *
 * @author pedroj
 */
public class AgenteConsola extends Agent {

    private ArrayList<Consola> myGui;
    private ArrayList<MensajeEnConsola> mensajesPendientes;
    private ContentManager manager = (ContentManager) getContentManager();
    //El lenguaje utilizado por el agente para la comunicacíon es SL
    private Codec codec = new SLCodec();
    //La ontologia utilizada por el agente
    private Ontology ontology;

    /**
     * Se ejecuta cuando se inicia el agente
     */
    @Override
    protected void setup() {
        //Incialización de variables
        myGui = new ArrayList();
        mensajesPendientes = new ArrayList();

        //Regisro de la Ontología
        try {
            ontology = ontologiaConsola.OntologiaPrimeraPractica.getInstance();
        } catch (BeanOntologyException ex) {
            ex.printStackTrace();
        }

        manager.registerLanguage(codec);
        manager.registerOntology(ontology);

        try {
            //Registro en Página Amarillas
            DFAgentDescription dfd = new DFAgentDescription();
            dfd.setName(getAID());
            ServiceDescription sd = new ServiceDescription();
            sd.setType(ontologiaConsola.OntologiaPrimeraPractica.SERVICIO_GUI);
            sd.setName(ontologiaConsola.OntologiaPrimeraPractica.AGENTE_CONSOLA);
            dfd.addServices(sd);
            // Los agentes que quieran comunicarse deben conocer la ontolgía "primera práctica"
            sd.addOntologies(ontologiaConsola.OntologiaPrimeraPractica.ONTOLOGY_NAME);
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        //Creamos la plantilla a emplear, para solo recibir mensajes con el protocolo FIPA_PROPOSE y la performativa PROPOSE
        MessageTemplate plantilla = ProposeResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_PROPOSE);

        // Se añaden las tareas principales
        this.addBehaviour(new RecepcionMensajes(this, plantilla));
    }

    /**
     * Se ejecuta al finalizar el agente
     */
    @Override
    protected void takeDown() {
        //Desregistro de las Páginas Amarillas
        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        //Se liberan los recuros y se despide
        cerrarConsolas();
        System.out.println("Finaliza la ejecución de " + this.getName());
    }

    //Métodos de utilidad para el agente consola
    private Consola buscarConsola(String nombreAgente) {
        // Obtenemos la consola donde se presentarán los mensajes
        Iterator<Consola> it = myGui.iterator();
        while (it.hasNext()) {
            Consola gui = it.next();
System.out.println("buscarConsola: "+gui.getNombreAgente());

            if (gui.getNombreAgente().compareTo(nombreAgente) == 0) {
                return gui;
            }
        }

        return null;
    }

    private void cerrarConsolas() {
        //Se eliminan las consolas que están abiertas
        Iterator<Consola> it = myGui.iterator();
        while (it.hasNext()) {
            Consola gui = it.next();
            gui.dispose();
        }
    }


    //Tareas del agente consola
    private class RecepcionMensajes extends ProposeResponder {

        public RecepcionMensajes(Agent _a, MessageTemplate _m) {
            super(_a, _m);
        }

        protected ACLMessage prepareResponse(ACLMessage propuesta) throws NotUnderstoodException {

            try {
                MensajeEnConsola mensaje = (MensajeEnConsola) manager.extractContent(propuesta);
                mensajesPendientes.add(mensaje);
                addBehaviour(new PresentarMensaje());

            } catch (Exception e) {
                e.printStackTrace();
            }
            ACLMessage agree = propuesta.createReply();
            agree.setPerformative(ACLMessage.ACCEPT_PROPOSAL);

            return agree;

        }
        
    }

    public class PresentarMensaje extends OneShotBehaviour {

        @Override
        public void action() {
            //Se coge el primer mensaje
            MensajeEnConsola mensajeConsola = mensajesPendientes.remove(0);

            //Se busca la ventana de consola o se crea una nueva
            Consola gui = buscarConsola(mensajeConsola.getNombreAgente());
            System.out.println("presentaMensaje: "+mensajeConsola.getNombreAgente());
            if (gui == null) {
                gui = new Consola(mensajeConsola.getNombreAgente());
                myGui.add(gui);
            }

            gui.presentarSalida(new MensajeConsola(mensajeConsola.getNombreAgente(), mensajeConsola.getContenido()));
        }

    }
}
