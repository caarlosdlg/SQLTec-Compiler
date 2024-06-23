/**
 * This class represents the functionality of the Intermediate Code Generator.
 * It is responsible for generating intermediate code based on the input program.
 *

 */
/*:-----------------------------------------------------------------------------
 *:                       INSTITUTO TECNOLOGICO DE LA LAGUNA
 *:                     INGENIERIA EN SISTEMAS COMPUTACIONALES
 *:                         LENGUAJES Y AUTOMATAS II           
 *: 
 *:        SEMESTRE: ______________            HORA: ______________ HRS
 *:                                   
 *:               
 *:    # Clase con la funcionalidad del Generador de COdigo Intermedio
 *                 
 *:                           
 *: Archivo       : GenCodigoInt.java
 *: Autor         : Fernando Gil  
 *: Fecha         : 03/SEP/2014
 *: Compilador    : Java JDK 7
 *: Descripción   :  
 *:                  
 *:           	     
 *: Ult.Modif.    :
 *:  Fecha      Modificó            Modificacion
 *:=============================================================================
 *:-----------------------------------------------------------------------------
 */


package compilador;

import general.Linea_BE;
import java.util.ArrayList;
import java.util.List;


public class GenCodigoInt {
 
    //Declarar las  constantes VACIO y ERROR_TIPO
    private final String VACIO = "vacio";
    private final String ERROR_TIPO = "error_tipo";
    private final String BOOLEAN = "boolean";
    private final List<List<String>> array_select = new ArrayList<>();
    

    private Compilador cmp;
    private boolean analizarSemantica = false;
    private String preAnalisis;

    private void emparejar(String t) {
        if (cmp.be.preAnalisis.complex.equals(t)) {
            cmp.be.siguiente();
            preAnalisis = cmp.be.preAnalisis.complex;
        } else {
            errorEmparejar(t, cmp.be.preAnalisis.lexema, cmp.be.preAnalisis.numLinea);
        }
    }

    //--------------------------------------------------------------------------
    // Metodo para devolver un error al emparejar
    //--------------------------------------------------------------------------
    private void errorEmparejar(String _token, String _lexema, int numLinea) {
        String msjError = "";

        if (_token.equals("id")) {
            msjError += "Se esperaba un identificador";
        } else if (_token.equals("num")) {
            msjError += "Se esperaba una constante entera";
        } else if (_token.equals("num.num")) {
            msjError += "Se esperaba una constante real";
        } else if (_token.equals("literal")) {
            msjError += "Se esperaba una literal";
        } else if (_token.equals("oparit")) {
            msjError += "Se esperaba un operador aritmetico";
        } else if (_token.equals("oprel")) {
            msjError += "Se esperaba un operador relacional";
        } else if (_token.equals("opasig")) {
            msjError += "Se esperaba operador de asignacion";
        } else {
            msjError += "Se esperaba " + _token;
        }
        msjError += " se encontró " + (_lexema.equals("$") ? "fin de archivo" : _lexema)
                + ". Linea " + numLinea;        // FGil: Se agregó el numero de linea

        cmp.me.error(Compilador.ERR_SINTACTICO, msjError);
    }

    // Fin de ErrorEmparejar
    //--------------------------------------------------------------------------
    // Metodo para mostrar un error sintactico
    private void error(String _descripError) {
        cmp.me.error(cmp.ERR_SINTACTICO, _descripError);
    }
    //--------------------------------------------------------------------------
    // Constructor de la clase, recibe la referencia de la clase principal del 
    // compilador.
    //
	public GenCodigoInt ( Compilador c ) {
        cmp = c;
    }
    // Fin del Constructor
    //--------------------------------------------------------------------------
	
    public void generar () {
        Atributos PROGRAMASQL = new Atributos();
        
        PROGRAMASQL ( PROGRAMASQL ); //PROCEDURE SIMBOLO INICIAL
    }    
    
    //--------------------------------------------------------------------------
    
    public void emite ( String c3d ){
        cmp.iuListener.mostrarCodInt(c3d);
    }
    
    
    //--------------------------------------------------------------------------

    //*****PEGAR LOS PROCEDURES DEL SINTACTICO
    // SIN ACCIONES SEMANTICAS
    
    
    //--------------------------------------------------------------------------

    private void PROGRAMASQL1 (){
        emite ("Este es una prueba, ya casi se acaba el semestre");
    }
    
   //--------------------------------------------------------------------------
    private void PROGRAMASQL (Atributos PROGRAMASQL ){
        Atributos DECLARACION = new Atributos();
        Atributos SENTENCIAS = new Atributos();
        
        emite ("clear\n" +
                    "clear all\n" +
                    "set talk off");
        
        if ( cmp.be.preAnalisis.complex.equals ( "declare" )  || cmp.be.preAnalisis.complex.equals ( "if" ) || 
            cmp.be.preAnalisis.complex.equals ( "while" ) || cmp.be.preAnalisis.complex.equals ( "print" ) || 
            cmp.be.preAnalisis.complex.equals ( "assign" ) || cmp.be.preAnalisis.complex.equals ( "select" ) ||
            cmp.be.preAnalisis.complex.equals ( "delete" ) || cmp.be.preAnalisis.complex.equals ( "insert" ) ||
            cmp.be.preAnalisis.complex.equals ( "update" ) || cmp.be.preAnalisis.complex.equals ( "create" ) ||
            cmp.be.preAnalisis.complex.equals ( "drop" ) || cmp.be.preAnalisis.complex.equals ( "case" ) ||
            cmp.be.preAnalisis.complex.equals( "end" ) ) {
           
            DECLARACION ( DECLARACION );
            SENTENCIAS ( SENTENCIAS );
            emparejar( "end" );
            //Inicio C3D
            PROGRAMASQL.codigo = DECLARACION.codigo + " " + SENTENCIAS.codigo; 
            //FinC3D
        } else {
            error ( "[PROGRAMASQL] El programa debe iniciar con "
                    + "palabra reservada como 'declare, if, while, print, assign,"
                    + "select, delete, insert, update, create, drop Ó case'. " 
                    + "N°Linea: " + cmp.be.preAnalisis.getNumLinea() );
        }
    }
    
    //--------------------------------------------------------------------------
    private void ACTREGS (Atributos ACTREGS){
        Linea_BE id = new Linea_BE();
        Atributos IGUALACION = new Atributos();
        Atributos EXPRCOND = new Atributos();
        
        if ( cmp.be.preAnalisis.complex.equals ( "update" ) ) {
            emparejar( "update" );
            id = cmp.be.preAnalisis; //Se guarda el atributo de id
            emparejar( "id" );
            emparejar( "set" );
            IGUALACION ( IGUALACION );
            emparejar( "where" );
            EXPRCOND ( EXPRCOND );
            //Inicio C3D
            emite ("USE "+ id.lexema.replace('@', '_') + "\n"
                    + "GO TOP " + "\n"
                    + "DO WHILE.NOT.EOF() " + "\n"
                    + "IF " + EXPRCOND.codigo + "\n" +"REPLACE "+ "promedio WITH _prom + 10 " + "\n" 
                    + "ENDIF " + "\n"
                    + "SKIP " + "\n"
                    + "ENDDO " + "\n"
                    + "BROW " + "\n"
                    + "USE "); 

            //Fin C3D
        } else {
            error ( "[ACTREGS] El texto debe iniciar con palabra reservada"
                    + "'update'. " 
                    + "N°Linea: " + cmp.be.preAnalisis.getNumLinea() );
        }
    }
    
    //--------------------------------------------------------------------------
    private void COLUMNAS (Atributos COLUMNAS){
        Atributos COLUMNASP = new Atributos();
        Linea_BE id = new Linea_BE();
        
        if ( cmp.be.preAnalisis.complex.equals ( "id" ) ) {
            id = cmp.be.preAnalisis;
            emparejar( "id" );
            COLUMNASP ( COLUMNASP );
            //Inicio C3D
            COLUMNAS.codigo = id.lexema.replace('@', '_') + " " + COLUMNASP.codigo;
            //Fin C3D
        } else {
            error ( "[COLUMNAS] El texto debe iniciar con una declaracion de "
                    + "variable." 
                    + "N°Linea: " + cmp.be.preAnalisis.getNumLinea() );
        }
    }
    
    //--------------------------------------------------------------------------
    private void COLUMNASP (Atributos COLUMNASP){
        Atributos COLUMNAS = new Atributos();
        if ( cmp.be.preAnalisis.complex.equals ( "," ) ) {
            // COLUMNASP -> , COLUMNAS {44}
            emparejar( "," );
            COLUMNAS ( COLUMNAS );
            //Inicio C3D
            COLUMNASP.codigo = ", " + COLUMNAS.codigo;
            //Fin C3D
        } else {
            //Inicio C3D
            COLUMNASP.codigo = "";
            //Fin C3D
        }
    }
    
    //--------------------------------------------------------------------------
    private void DECLARACION (Atributos DECLARACION){
        Linea_BE idvar = new Linea_BE();
        Atributos TIPO = new Atributos();
        Atributos DECLARACION1 = new Atributos();
        
        if ( cmp.be.preAnalisis.complex.equals ( "declare" ) ) {
            emparejar( "declare" );
            idvar = cmp.be.preAnalisis;
            emparejar( "idvar" );
            //Inicio C3D
           
            emite ("PUBLIC " + idvar.lexema.replace('@', '_'));
            //Fin C3D
            TIPO ( TIPO );
            DECLARACION ( DECLARACION1 );
        }else {
            // DECLARACION -> empty
        }
    }
    
    //--------------------------------------------------------------------------
    private void DESPLIEGUE (Atributos DESPLIEGUE){
        Atributos EXPRARIT = new Atributos();
        
        if ( cmp.be.preAnalisis.complex.equals ( "print" ) ) {
            emparejar( "print" );
            EXPRARIT ( EXPRARIT );
            //Inicio C3D
            emite ("? " + EXPRARIT.codigo);
            //Fin C3D
        } else {
            error ( "[DESPLIEGUE] El texto debe iniciar con la palabra "
                    + "reservada 'print'. " 
                    + "N°Linea: " + cmp.be.preAnalisis.getNumLinea() );
        }
    }
    
    //--------------------------------------------------------------------------
    private void DELREG (Atributos DELREG){
        Atributos EXPRCOND = new Atributos();
        Linea_BE id = new Linea_BE();
        
        if ( cmp.be.preAnalisis.complex.equals ( "delete" ) ) {
            emparejar( "delete" );
            emparejar( "from" );
            id = cmp.be.preAnalisis;
            emparejar( "id" );
            emparejar( "where" );
            EXPRCOND ( EXPRCOND );
            //Inicio C3D
            emite("USE " + id.lexema.replace('@', '_') + "\n"
                    + "GO TOP" + "\n"
                    + "DOM WHILE .NOT.EOF() " + "\n"
                    + "IF " + EXPRCOND.codigo + "\n"
                    + "DELETE " + "\n"
                    + "ENDIF " + "\n"
                    + "SKIP " + "\n"
                    + "ENDDO " + "\n"
                    + "BROW " + "\n"
                    + "USE ");

            //Fin C3D
        } else {
            error ( "[DELREG] El texto debe iniciar con la palabra "
                    + "reservada 'delete'. " 
                    + "N°Linea: " + cmp.be.preAnalisis.getNumLinea() );
        }
    }
    
    //--------------------------------------------------------------------------
    private void EXPRESIONES (Atributos EXPRESIONES){
        Atributos EXPRARIT = new Atributos();
        Atributos EXPRESIONESP = new Atributos();
        
        if ( cmp.be.preAnalisis.complex.equals ( "num" ) || cmp.be.preAnalisis.complex.equals ( "num.num" ) ||
             cmp.be.preAnalisis.complex.equals ( "idvar" ) || cmp.be.preAnalisis.complex.equals ( "literal" ) ||
             cmp.be.preAnalisis.complex.equals ( "id" ) || cmp.be.preAnalisis.complex.equals ( "(" ) || 
             cmp.be.preAnalisis.complex.equals ( "," ) ) {
            EXPRARIT (EXPRARIT);
            EXPRESIONESP (EXPRESIONESP);
            //Inicio C3D
            EXPRESIONES.codigo = EXPRARIT.codigo + " " + EXPRESIONESP.codigo ;
            //Fin C3D
            
        } else {
            error ( "[EXPRESIONES] El texto debe ser ser uno de los siguientes "
                    + "tipos: num, num.num, idvar, literal, id, '(', ','. " 
                    + "N°Linea: " + cmp.be.preAnalisis.getNumLinea() );
        }
    }
    
    //--------------------------------------------------------------------------
    private void EXPRESIONESP (Atributos EXPRESIONESP) {
        Atributos EXPRESIONES = new Atributos();
        
        if ( cmp.be.preAnalisis.complex.equals ( "," ) ) {
            emparejar( "," );
            EXPRESIONES (EXPRESIONES);
            //Inicio C3D
            EXPRESIONESP.codigo = ", " + EXPRESIONES.codigo ;
            //Fin C3D
        } else {
            //Inicio C3D
            EXPRESIONESP.codigo = "";
            //Fin C3D
        }
    }
    
    //--------------------------------------------------------------------------
    private void EXPRARIT (Atributos EXPRARIT){
        Atributos OPERANDO = new Atributos();
        Atributos EXPRARIT1 = new Atributos();
        Atributos EXPRARITP = new Atributos();
        
        if ( cmp.be.preAnalisis.complex.equals ( "num" ) || cmp.be.preAnalisis.complex.equals ( "num.num" ) ||
             cmp.be.preAnalisis.complex.equals ( "idvar" ) || cmp.be.preAnalisis.complex.equals ( "literal" ) ||
             cmp.be.preAnalisis.complex.equals ( "id" ) ) {
            OPERANDO (OPERANDO);
            EXPRARITP (EXPRARITP);
            //Inicio C3D
            EXPRARIT.codigo = OPERANDO.codigo + " " + EXPRARITP.codigo;
            //Fin C3D
        } else if ( cmp.be.preAnalisis.complex.equals ( "(") ) {
            emparejar( "(" );
            EXPRARIT (EXPRARIT1);
            emparejar( ")" );
            EXPRARITP (EXPRARITP);
            //Inicio C3D
            EXPRARIT.codigo = EXPRARIT1.codigo + " " + EXPRARITP.codigo;
            //Fin C3D
        }else {
            error ( "[EXPRARIT] El texto debe ser ser uno de los siguientes "
                    + "tipos: num, num.num, idvar, literal, un id "
                    + "ó empezar con '('." 
                    + "N°Linea: " + cmp.be.preAnalisis.getNumLinea() );
        }
    }
    
    //--------------------------------------------------------------------------
    private void EXPRARITP (Atributos EXPRARITP){
        Atributos EXPRARIT = new Atributos();
        Linea_BE opsuma = new Linea_BE();
        Linea_BE opmult = new Linea_BE();
        
        if ( cmp.be.preAnalisis.complex.equals ( "opsuma" ) ) {
            opsuma = cmp.be.preAnalisis;
            emparejar( "opsuma" );
            EXPRARIT (EXPRARIT);
            //Inicio C3D
            EXPRARITP.codigo = opsuma.lexema + " " + EXPRARIT.codigo ;
            //Fin C3D
        } else if ( cmp.be.preAnalisis.complex.equals ( "opmult" ) ) {
            opmult = cmp.be.preAnalisis;
            emparejar( "opmult" );
            EXPRARIT (EXPRARIT);
            //Inicio C3D
            EXPRARITP.codigo = opmult.lexema + " " + EXPRARIT.codigo ;
            //Fin C3D
        } else {
            //Inicio C3D
            EXPRARITP.codigo = "";
            //Fin C3D
        }
    }
    
    //--------------------------------------------------------------------------
    private void EXPRCOND (Atributos EXPRCOND) {
        Atributos EXPRREL = new Atributos();
        Atributos EXPRLOG = new Atributos();
        
        if ( cmp.be.preAnalisis.complex.equals ( "num" ) || cmp.be.preAnalisis.complex.equals ( "num.num" ) ||
             cmp.be.preAnalisis.complex.equals ( "idvar" ) || cmp.be.preAnalisis.complex.equals ( "literal" ) ||
             cmp.be.preAnalisis.complex.equals ( "id" ) || cmp.be.preAnalisis.complex.equals ( "(" ) || 
             cmp.be.preAnalisis.complex.equals ( "and" ) || cmp.be.preAnalisis.complex.equals ( "or" ) ) {
            EXPRREL (EXPRREL);
            EXPRLOG (EXPRLOG);
            //Inicio C3D
            EXPRCOND.codigo = EXPRREL.codigo + " " + EXPRLOG.codigo;
            //Fin C3D
        } else {
            error ( "[EXPRCOND] El texto debe ser ser uno de los siguientes "
                    + "tipos: num, num.num, idvar, literal, id, '(', and, or. " 
                    + "N°Linea: " + cmp.be.preAnalisis.getNumLinea() );
        }
    }
    
    //--------------------------------------------------------------------------
    private void EXPRREL (Atributos EXPRREL) {
        Atributos EXPRARIT1 = new Atributos();
        Atributos EXPRARIT2 = new Atributos();
        Linea_BE oprel = new Linea_BE();
        
        if ( cmp.be.preAnalisis.complex.equals ( "num" ) || cmp.be.preAnalisis.complex.equals ( "num.num" ) ||
             cmp.be.preAnalisis.complex.equals ( "idvar" ) || cmp.be.preAnalisis.complex.equals ( "literal" ) ||
             cmp.be.preAnalisis.complex.equals ( "id" ) ) {
           
            EXPRARIT (EXPRARIT1);
            oprel = cmp.be.preAnalisis;
            emparejar( "oprel" );
            EXPRARIT (EXPRARIT2);
            //Inicio C3D
            EXPRREL.codigo = EXPRARIT1.codigo  + " " + oprel.lexema 
                    + " " + EXPRARIT2.codigo;
            //Fin C3D
        } else {
            error ( "[EXPRREL] El texto debe ser ser uno de los siguientes "
                    + "tipos: num, num.num, idvar, literal o un identificador. " 
                    + "N°Linea: " + cmp.be.preAnalisis.getNumLinea() );
        }
    }
    
    //--------------------------------------------------------------------------
    private void EXPRLOG (Atributos EXPRLOG) {
        Atributos EXPRREL = new Atributos();
        
        if ( cmp.be.preAnalisis.complex.equals ( "and" ) ) {
            emparejar( "and" );
            EXPRREL (EXPRREL);
            //Inicio C3D
            EXPRLOG.codigo = ".AND. " + EXPRREL.codigo;
            //Fin C3D
        } else if ( cmp.be.preAnalisis.complex.equals ( "or") ) {
            emparejar( "or" );
            EXPRREL (EXPRREL);
            //Inicio C3D
            EXPRLOG.codigo = ".OR. " + EXPRREL.codigo;
            //Fin C3D
        } else {
            //Inicio C3D
            EXPRLOG.codigo = "";
            //Fin C3d
        }
    }
    
    //--------------------------------------------------------------------------
    private void ELIMTAB (Atributos ELIMTAB) {
        Linea_BE id = new Linea_BE();
        
        if ( cmp.be.preAnalisis.complex.equals ( "drop" ) ) {
            emparejar( "drop" );
            emparejar( "table" );
            id = cmp.be.preAnalisis;
            emparejar( "id" );
            //Inicio C3D
            emite ( "DELETE FILE " + id.lexema  + ".DBF" );
            //Fin
        } else {
            error ( "[ELIMTAB] El texto debe iniciar con la palabra "
                    + "reservada 'drop'. " 
                    + "N°Linea: " + cmp.be.preAnalisis.getNumLinea() );
        }
    }
    
    //--------------------------------------------------------------------------
    private void IFELSE (Atributos IFELSE) {
        Atributos EXPRCOND = new Atributos();
        Atributos SENTENCIAS = new Atributos();
        Atributos IFELSEP = new Atributos();
        
        if ( cmp.be.preAnalisis.complex.equals ( "if" ) ) {
            emparejar( "if" );
            EXPRCOND (EXPRCOND);
            emparejar( "begin" );
            //Inicio C3D
            emite ( "IF " + EXPRCOND.codigo );
            //Fin C3D
            SENTENCIAS (SENTENCIAS);
            emparejar( "end" );
            IFELSEP (IFELSEP);
        } else {
            error ( "[IFELSE] El texto debe iniciar con la palabra "
                    + "reservada 'if'. " 
                    + "N°Linea: " + cmp.be.preAnalisis.getNumLinea() );
        }
    }
    
    //--------------------------------------------------------------------------
    private void IFELSEP (Atributos IFELSEP) {
        Atributos SENTENCIAS = new Atributos();
        
        if ( cmp.be.preAnalisis.complex.equals ( "else" ) ) {
            emparejar( "else" );
            emparejar( "begin" );
            //Inicio C3D
            emite ( " ELSE " );
            //Fin C3D
            SENTENCIAS (SENTENCIAS);
            emparejar( "end" );
            //Inicio C3D
            emite ( " ENDIF " );
            //Fin
        } else {
            //Inicio C3D
            emite ( " ENDIF " );
            //Fin
        }
    }
    
    //--------------------------------------------------------------------------
    private void IGUALACION (Atributos IGUALACION) {
        Linea_BE id = new Linea_BE();
        Linea_BE opasig = new Linea_BE();
        Atributos EXPRARIT = new Atributos();
        Atributos IGUALACIONP = new Atributos();
        
        if ( cmp.be.preAnalisis.complex.equals ( "id" ) ) {
            id = cmp.be.preAnalisis; //Se guarda el atributo de id
            emparejar( "id" );
            opasig = cmp.be.preAnalisis; //Se guarda el atributo de opasig
            emparejar( "opasig" );
            EXPRARIT (EXPRARIT);
            IGUALACIONP (IGUALACIONP);
            //Inicio C3D
            IGUALACION.codigo = id.lexema.replace('@', '_') + " " + opasig.lexema +" " 
                    + EXPRARIT.codigo + " " + IGUALACIONP.codigo;
            //Fin C3D
        } else {
            error ( "[IGUALACION] El texto debe iniciar con un identificador. " 
                    + "N°Linea: " + cmp.be.preAnalisis.getNumLinea() );
        }
    }
    
    //--------------------------------------------------------------------------
    private void IGUALACIONP (Atributos IGUALACIONP) {
        Atributos IGUALACION = new Atributos();
        
        if ( cmp.be.preAnalisis.complex.equals ( "," ) ) {
            emparejar( "," );
            IGUALACION (IGUALACION);
            //Inicio C3D
            IGUALACIONP.codigo = ", " + IGUALACION.codigo;
            //Fin C3D
        } else {
            //Inicio C3D
            IGUALACIONP.codigo = "";
            //Fin
        }
    }
    
    //--------------------------------------------------------------------------
    private void INSERCION (Atributos INSERCION) {
        Atributos COLUMNAS = new Atributos();
        Atributos EXPRESIONES = new Atributos();
        Linea_BE id = new Linea_BE();
        
        if ( cmp.be.preAnalisis.complex.equals( "insert" ) ) {
            emparejar ( "insert" );
            emparejar ( "into" );
            id = cmp.be.preAnalisis;
            emparejar ( "id" );
            emparejar ( "(" );
            COLUMNAS (COLUMNAS) ;
            emparejar ( ")" );
            emparejar ( "values" );
            emparejar ( "(" );
            EXPRESIONES (EXPRESIONES);
            emparejar ( ")" );
            //Inicio C3D
            emite("USE " + id.lexema.replace('@', '_') + "\n"
                    + "APPEND BLANK" + "\n"
                    + "REPLACE " + forma(COLUMNAS.codigo, EXPRESIONES.codigo) );
            //Fin C3D
        }
        else {
            error ("[INSERCION] el texto debe de iniciar con la palabra insert"
                  + "en la linea Numero " + cmp.be.preAnalisis.getNumLinea() ) ;
        }
        
    }
    
    //--------------------------------------------------------------------------
    private void LISTAIDS (Atributos LISTAIDS) {
        Linea_BE id = new Linea_BE();
        Atributos LISTAIDS1 = new Atributos();
        
        if ( cmp.be.preAnalisis.complex.equals( "," ) ) {
             emparejar ( "," );
             id = cmp.be.preAnalisis; //ID GUARDADA
             emparejar ( "id" );
             LISTAIDS (LISTAIDS1) ;
             //Inicio C3D
             LISTAIDS.codigo = id.lexema.replace('@', '_') + " " + LISTAIDS1.codigo;
             //Fin C3D
        }
        else {
            //Inicio C3D
             LISTAIDS.codigo = "";
             //Fin
        }
    }
    
    //--------------------------------------------------------------------------
    private void NULO(Atributos NULO) {

        if (cmp.be.preAnalisis.complex.equals("null")) {
            emparejar("null");
            //Inicio C3D
            NULO.codigo = " NULL ";
            //Fin C3D
        } else if (cmp.be.preAnalisis.complex.equals("not")) {
            emparejar("not");
            emparejar("null");
            //Inicio C3D
            NULO.codigo = " NOT NULL ";
            //Fin C3D
        } else {
            //Inicio C3D
            NULO.codigo = "";
            //Fin C3D
        }

    }
    
    //--------------------------------------------------------------------------
    private void OPERANDO (Atributos OPERANDO) {
        Linea_BE num = new Linea_BE();
        Linea_BE numnum = new Linea_BE();
        Linea_BE idvar = new Linea_BE();
        Linea_BE literal = new Linea_BE();
        Linea_BE id = new Linea_BE();
        
        if (cmp.be.preAnalisis.complex.equals("num")) {
            num = cmp.be.preAnalisis; //ID GUARDADA
            emparejar("num");
            //Inicio C3D
            OPERANDO.codigo = num.lexema;
            //Fin C3D
        } else if (cmp.be.preAnalisis.complex.equals("num.num")) {
            numnum = cmp.be.preAnalisis; //ID GUARDADA
            emparejar("num.num");
            //Inicio C3D
            OPERANDO.codigo = numnum.lexema;
            //Fin C3D
        } else if (cmp.be.preAnalisis.complex.equals("idvar")) {
            idvar = cmp.be.preAnalisis; //ID GUARDADA
            emparejar("idvar");
            //Inicio C3D
            OPERANDO.codigo = idvar.lexema.replace('@', '_');
            //Fin
        } else if (cmp.be.preAnalisis.complex.equals("literal")) {
            literal = cmp.be.preAnalisis; //ID GUARDADA
            emparejar("literal");
            //Inicio C3D
            OPERANDO.codigo = literal.lexema;
            //Fin
        } else if (cmp.be.preAnalisis.complex.equals("id")) {
            id = cmp.be.preAnalisis; //ID GUARDADA
            emparejar("id");
            //Inicio C3D
            OPERANDO.codigo = id.lexema.replace('@', '_');
            //Fin 
        } else {
            error("[OPERANDO], se esperaba recibir un valor de numero entero, "
                    + "numero de punto flotante, un identificador de variable "
                    + "una literal o un identificador "
                    + "en la linea " + cmp.be.preAnalisis.getNumLinea());
        }
    }
    //--------------------------------------------------------------------------
    private void SENTENCIAS (Atributos SENTENCIAS) {
        Atributos SENTENCIA = new Atributos();
        Atributos SENTENCIAS1 = new Atributos();
        
       if ( cmp.be.preAnalisis.complex.equals( "if" ) ||
            cmp.be.preAnalisis.complex.equals( "while" ) ||
            cmp.be.preAnalisis.complex.equals( "print" ) ||
            cmp.be.preAnalisis.complex.equals( "assign" ) || 
            cmp.be.preAnalisis.complex.equals( "select" ) ||
            cmp.be.preAnalisis.complex.equals( "delete" ) ||
            cmp.be.preAnalisis.complex.equals( "insert" ) ||
            cmp.be.preAnalisis.complex.equals( "update" ) ||
            cmp.be.preAnalisis.complex.equals( "create" ) ||
            cmp.be.preAnalisis.complex.equals( "drop" ) ||
            cmp.be.preAnalisis.complex.equals( "case" ) ) {
           SENTENCIA (SENTENCIA);
           SENTENCIAS (SENTENCIAS1);
           //Inicio C3D
           SENTENCIAS.codigo = SENTENCIA.codigo + " " + SENTENCIAS1.codigo;
           //Fin
       }
       else {
           //Inicio C3D
           SENTENCIAS.codigo = "";
           //Fin
       }
}
    //--------------------------------------------------------------------------
    private void SENTENCIA(Atributos SENTENCIA) {
        Atributos IFELSE = new Atributos();
        Atributos SENREP = new Atributos();
        Atributos DESPLIEGUE = new Atributos();
        Atributos SENTASIG = new Atributos();
        Atributos SENTSELECT = new Atributos();
        Atributos DELREG = new Atributos();
        Atributos INSERCION = new Atributos();
        Atributos ACTREGS = new Atributos();
        Atributos TABLA = new Atributos();
        Atributos ELIMTAB = new Atributos();
        Atributos SELECTIVA = new Atributos();
        Atributos EXPRREL = new Atributos();
        
        if (cmp.be.preAnalisis.complex.equals("if")) {
            IFELSE(IFELSE);
            //Inicio C3D
            SENTENCIA.codigo = IFELSE.codigo;
            //Fin
        } else if (cmp.be.preAnalisis.complex.equals("while")) {
            SENREP(SENREP);
            //Inicio C3D
            SENTENCIA.codigo = SENREP.codigo;
            //Fin
        } else if (cmp.be.preAnalisis.complex.equals("print")) {
            DESPLIEGUE(DESPLIEGUE);
            //Inicio C3D
            SENTENCIA.codigo = DESPLIEGUE.codigo;
            //Fin
        } else if (cmp.be.preAnalisis.complex.equals("assign")) {
            SENTASIG(SENTASIG);
            //Inicio C3D
            SENTENCIA.codigo = SENTASIG.codigo;
            //Fin
        } else if (cmp.be.preAnalisis.complex.equals("select")) {
            SENTSELECT(SENTSELECT);
            //Inicio C3D
            SENTENCIA.codigo = DESPLIEGUE.codigo;
            //Fin
        } else if (cmp.be.preAnalisis.complex.equals("delete")) {
            DELREG(DELREG);
            //Incio C3D
            SENTENCIA.codigo = DELREG.codigo;
            //Fin
        } else if (cmp.be.preAnalisis.complex.equals("insert")) {
            INSERCION(INSERCION);
            //Inicio C3D
            SENTENCIA.codigo = INSERCION.codigo;
            //FIn
        } else if (cmp.be.preAnalisis.complex.equals("update")) {
            ACTREGS(ACTREGS);
            //Inicio C3D
            SENTENCIA.codigo = ACTREGS.codigo;
            //Fin
        } else if (cmp.be.preAnalisis.complex.equals("create")) {
            TABLA(TABLA);
            //Inicio C3D
            SENTENCIA.codigo = TABLA.codigo;
            //Fin 
        } else if (cmp.be.preAnalisis.complex.equals("drop")) {
            ELIMTAB(ELIMTAB);
            //Inicio C3D
            SENTENCIA.codigo = ELIMTAB.codigo;
            //Fin
        } else if (cmp.be.preAnalisis.complex.equals("case")) {
            SELECTIVA(SELECTIVA);
            //Inicio C3D
            SENTENCIA.codigo = SELECTIVA.codigo;
            //fin
        } else if (cmp.be.preAnalisis.complex.equals("and")) {
            emparejar("and");
            EXPRREL(EXPRREL);
            //Inicio C3D
            SENTENCIA.codigo = EXPRREL.codigo;
            //Fin
        } else if (cmp.be.preAnalisis.complex.equals("or")) {
            emparejar("or");
            EXPRREL(EXPRREL);
            //Inicio C3D
            SENTENCIA.codigo = EXPRREL.codigo;
            //Fin
        } else {
            error("[SENTENCIA] hubo un error en la creacion de sentencias"
                    + "ya que no contiene o estan mal escrita la sentencia de instruccion"
                    + "[if, while, print, assign, select, delete, insert, update, create, drop, case] "
                    + "en la linea " + cmp.be.preAnalisis.getNumLinea());
        }
    }
    
    //--------------------------------------------------------------------------
    private void SELECTIVA (Atributos SELECTIVA) {
        Atributos SELWHEN = new Atributos();
        Atributos SELELSE = new Atributos();
        
        if ( cmp.be.preAnalisis.complex.equals( "case") ) {
            emparejar ("case");
            //Inicio C3D
            emite ( "DO CASE " );
            //Fin
            SELWHEN (SELWHEN);
            SELELSE (SELELSE);
            emparejar ( "end" );
            //Inicio C3D
            emite ( "ENDCASE " );
            //Fin
        }
        else {
            error ( "[SELECTIVA] se esperaba la palabra 'case' "
                    + "de los distintos casos de seleccion a usar "
                    + "en la linea " + cmp.be.preAnalisis.getNumLinea() );
        }
    }
    
    //--------------------------------------------------------------------------
    private void SELWHEN (Atributos SELWHEN) {
        Atributos EXPRCOND = new Atributos();
        Atributos SENTENCIA = new Atributos();
        Atributos SELWHENP = new Atributos();
        
        if ( cmp.be.preAnalisis.complex.equals( "when" ) ){
            emparejar ( "when" );
            EXPRCOND (EXPRCOND);
            //Inicio C3D
            emite ( " CASE " + EXPRCOND.codigo );
            //Fin
            emparejar ( "then" );
            SENTENCIA (SENTENCIA);
            SELWHENP (SELWHENP);
        }
        else {
            error ( "[SELWHEN] Se esperaba la palabra 'when' seguida de una "
                    + "expresion condicional "
                    + "en la linea " + cmp.be.preAnalisis.getNumLinea() );
        }
    }
    
    //--------------------------------------------------------------------------
    private void SELWHENP (Atributos SELWHENP) {
        Atributos SELWHEN = new Atributos();
        
        if ( cmp.be.preAnalisis.complex.equals( "when" ) ){
            SELWHEN (SELWHEN);
            //Inicio C3D
            SELWHENP.codigo = SELWHEN.codigo;
            //Fin
        } else {
            //Inicio C3D
            SELWHENP.codigo = "";
            //Fin 
        }
    }
    
    //--------------------------------------------------------------------------
    //o
    private void SELELSE (Atributos SELELSE) {
        //VAARIABLES LOCALES
        Atributos SENTENCIA = new Atributos();
        
        if ( cmp.be.preAnalisis.complex.equals( "else" ) ){
            // SELELSE -> else SENTENCIA
            emparejar ( "else" );
            SENTENCIA (SENTENCIA);
            //INICIO C3D
            SELELSE.codigo = " OTHERWISE ";
            //FIN
        } 
        else {
            //Inicio C3D
            SELELSE.codigo = "";
            //FIn
        }
        
    }
    
    //--------------------------------------------------------------------------
    private void SENREP (Atributos SENREP) {
        Atributos EXPRCOND = new Atributos();
        Atributos SENTENCIAS = new Atributos();
        
        if ( cmp.be.preAnalisis.complex.equals( "while" ) ) {
            emparejar ( "while" );
            EXPRCOND (EXPRCOND);
            emparejar ( "begin" );
            //Inicio C3D
            emite ( " DO WHILE " + EXPRCOND.codigo );
            //Fin C3D
            SENTENCIAS (SENTENCIAS);
            emparejar ( "end" );
            //Inicio C3D
            emite ( "ENDDO ");
            //Fin C3D
        } 
        else {
            error ( "[SENREP] se esperaba la palabra 'while' seguida de una Expresion Condicional "
                    + "en la linea " + cmp.be.preAnalisis.getNumLinea());
        }
        
    }
    
    //--------------------------------------------------------------------------
    private void SENTASIG (Atributos SENTASIG) {
        Linea_BE idvar = new Linea_BE();
        Atributos EXPRARIT = new Atributos();
        
        if ( cmp.be.preAnalisis.complex.equals( "assign") ) {
            emparejar ( "assign" );
            idvar = cmp.be.preAnalisis;
            emparejar ( "idvar" );
            emparejar ( "opasig" );
            EXPRARIT (EXPRARIT);
            //Inicio C3D
            emite ( "STORE " + EXPRARIT.codigo + " TO " + idvar.lexema.replace('@', '_'));
            //Fin C3D
        } else {
            error ( "[SENTASIG] se esperaba la palabra 'assign' seguida de una "
                    + "variable de identificador en la linea "
                    + cmp.be.preAnalisis.getNumLinea() );
        }
    }
    
    //--------------------------------------------------------------------------
    private void SENTSELECT(Atributos SENTSELECT) {
        Atributos SENTSELECTC = new Atributos();
        Atributos EXPRCOND = new Atributos();
        Linea_BE id1 = new Linea_BE();
        Linea_BE idvar = new Linea_BE();
        Linea_BE id2 = new Linea_BE();

        if (cmp.be.preAnalisis.complex.equals("select")) {
            emparejar("select");
            idvar = cmp.be.preAnalisis;
            emparejar("idvar");
            emparejar("opasig");
            id2 = cmp.be.preAnalisis;
            emparejar("id");
            SENTSELECTC(SENTSELECTC);
            emparejar("from");
            id1 = cmp.be.preAnalisis;
            emparejar("id");
            emparejar("where");
            EXPRCOND(EXPRCOND);
            //Inicio C3D
            emite( "USE " + id1.lexema.replace('@', '_') + "\n"
                    + "GO TOP " + "\n"
                    + "DO WHILE.NOT.EOF() " + "\n"
                    + "IF " + EXPRCOND.codigo + "\n"
                    + "STORE " + id2.lexema.replace('@', '_') + " TO " + idvar.lexema.replace('@', '_') + "\n"
                    + SENTSELECTC.codigo 
                    + "ENDIF " + "\n"
                    + "SKIP " + "\n"
                    + "ENDDO " + "\n"
                    + "BROW " + "\n"
                    + "USE ");

            //Fin C3D
        } else {
            error("[SENTSELECT] se esperaba la palabra select seguida de la "
                    + "siguiente estructura: "
                    + "[idvar opasig id SENTSELECTC from id where EXPRCOND] "
                    + "en la linea " + cmp.be.preAnalisis.getNumLinea());
        }
    }
    
    //--------------------------------------------------------------------------
    private void SENTSELECTC (Atributos SENTSELECTC) {
        Linea_BE idvar = new Linea_BE();
        Linea_BE id = new Linea_BE();
        Atributos SENTSELECTC1 = new Atributos();
        
        if ( cmp.be.preAnalisis.complex.equals( "," ) ) {
            emparejar ( "," );
            idvar = cmp.be.preAnalisis;
            emparejar ( "idvar" );
            emparejar ( "opasig" );
            id = cmp.be.preAnalisis;
            emparejar ( "id" );
            SENTSELECTC (SENTSELECTC1);
            //Inicio C3D
            SENTSELECTC.codigo = "STORE "+ id.lexema.replace('@', '_') +" TO " 
                    + idvar.lexema.replace('@', '_') + "\n" + SENTSELECTC1.codigo;
            //Fin C3D
        } else {
            // SENTSELECTC -> empty {68}
            //Inicio C3D
            SENTSELECTC.codigo = "";
            //Fin C3D
        } 
        
    }
    
    //--------------------------------------------------------------------------
    private void TIPO(Atributos TIPO) {
        Linea_BE num = new Linea_BE();

        if (cmp.be.preAnalisis.complex.equals("int")) {
            emparejar("int");
            //Inicio C3D
            TIPO.codigo = "N( 5 )";
            //Fin C3D
        } else if (cmp.be.preAnalisis.complex.equals("float")) {
            emparejar("float");
            //Inicio C3D
            TIPO.codigo = "N( 8,3 )";
            //Fin C3D
        } else if (cmp.be.preAnalisis.complex.equals("char")) {
            emparejar("char");
            emparejar("(");
            num = cmp.be.preAnalisis;
            emparejar("num");
            emparejar(")");
            //Inicio C3D
            TIPO.codigo = "C( "+ num.lexema + " )";
            //Fin C3D
        } else {
            error("[TIPO] error al detectar que tipo de dato "
                    + "se va recibir [int, floar, char ( num )] "
                    + "en la linea " + cmp.be.preAnalisis.getNumLinea());
        }

    }
    
    //--------------------------------------------------------------------------
    private void TABLA (Atributos TABLA) {
        Linea_BE id = new Linea_BE();
        Atributos TABCOLUMNAS = new Atributos();
        
        if ( cmp.be.preAnalisis.complex.equals( "create" ) ) {
            emparejar ( "create" );
            emparejar ( "table" );
            id = cmp.be.preAnalisis;
            emparejar ( "id" );
            emparejar ( "(" );
            TABCOLUMNAS (TABCOLUMNAS) ;
            emparejar ( ")" );
            //Inicio C3D
            emite ( "CREATE TABLE " + id.lexema.replace('@', '_') + " ("+ TABCOLUMNAS.codigo + ")" );
            //Fin C3D
        }
        else {
            error ("[TABLA] se esperaba la palabra create en la linea "
                    + cmp.be.preAnalisis.getNumLinea() ) ;
        }
    }
    
    //--------------------------------------------------------------------------
    private void TABCOLUMNAS(Atributos TABCOLUMNAS) {
        Linea_BE id = new Linea_BE();
        Atributos TIPO = new Atributos();
        Atributos NULO = new Atributos();
        Atributos TABCOLUMNASP = new Atributos();
        
        if ( cmp.be.preAnalisis.complex.equals( "id" ) ) {
            id = cmp.be.preAnalisis;
            emparejar ( "id" );
            TIPO (TIPO);
            NULO (NULO);
            TABCOLUMNASP(TABCOLUMNASP);
            //Inicio C3D
            TABCOLUMNAS.codigo = id.lexema.replace('@', '_') + " " + TIPO.codigo + " " 
                    + NULO.codigo + " " + TABCOLUMNASP.codigo;
            //Fin C3D
        }
        else {
            error ("[TABCOLUMNAS] se esperaba la palabra id seguida del tipo de dato "
                    + "que se va a declarar en la linea "
                    + cmp.be.preAnalisis.getNumLinea()) ;
        }
    }
    
    //--------------------------------------------------------------------------
    private void TABCOLUMNASP (Atributos TABCOLUMNASP) {
        Atributos TABCOLUMNAS = new Atributos();
        
        if ( cmp.be.preAnalisis.complex.equals( "," ) ) {
            emparejar ( "," );
            TABCOLUMNAS (TABCOLUMNAS);
            //Inicio C3D
            TABCOLUMNASP.codigo = ", " + TABCOLUMNAS.codigo; 
            //Fin C3D
        }
        else {
            //Inicio C3D
            TABCOLUMNASP.codigo = "";
            //Fin C3D
        }
    }
    
    private String forma(String columas, String valores) {
        String [] cols = columas.split(",");
        String [] vals = valores.split(",");
        StringBuilder sb = new StringBuilder();
        for ( int i = 0; i < cols.length; i++){
            if( i > 0) {
                sb.append(", ");
            }
            sb.append(cols[i].trim()).append(" WITH ").append(vals[i].trim());
        }
        return sb.toString();
    } 
    
}
