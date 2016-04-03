/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ontologiaConsola;

import jade.content.onto.BeanOntology;
import jade.content.onto.BeanOntologyException;
import jade.content.onto.Ontology;

/**
 *
 * @author pedroj
 */
public class OntologiaPrimeraPractica extends BeanOntology {
    
    private static final long serialVersionUID = 1L;

    // NOMBRE
    public static final String ONTOLOGY_NAME = "Ontologia_Primeara_Practica";
        
    //VOCABULARIO
    public static String SERVICIO_GUI = "GUI";
    public static String AGENTE_CONSOLA = "Consola";
    public static String SERVICIO_UTILIDAD = "Utilidad";
    public static String AGENTE_OPERADOR = "Operador";
    
    public static String OPERACION_SUMA = "Suma";
    public static String OPERACION_RESTA = "Resta";
    public static String OPERACION_MULTIPLICACION = "Multiplicación";
    public static String OPERACION_DIVISION = "Division";
    
    public static String OPERACION_NO_DEFINIDA = "Operación no definida";
    public static String OPERACION_NO_DISPONIBLE = "Operación no disponible";   
    
    // The singleton instance of this ontology
    private static Ontology INSTANCE;

    public synchronized final static Ontology getInstance() throws BeanOntologyException {
        
        if (INSTANCE == null) {
            INSTANCE = new OntologiaPrimeraPractica();
	}
            return INSTANCE;
    }

    /**
     * Constructor
     * 
     * @throws BeanOntologyException
     */
    private OntologiaPrimeraPractica() throws BeanOntologyException {
	
        super(ONTOLOGY_NAME);
        
        add("ontologia.elementos");
    }
}
