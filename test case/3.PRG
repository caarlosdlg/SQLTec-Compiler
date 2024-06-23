PUBLIC _control
PUBLIC _nombre
PUBLIC _edad
PUBLIC _prom
USE alumnos
GO TOP 
DO WHILE.NOT.EOF() 
IF promedio  > 70  
STORE control TO _control
STORE nombre TO _nombre
STORE edad TO _edad
STORE promedio TO _prom
ENDIF 
SKIP 
ENDDO 
BROW 
USE 
? 'Control: ' 
? _control 
? 'Nombre : ' + _nombre 
USE alumnos
GO TOP 
DO WHILE.NOT.EOF() 
IF nombre  = 'David'  REPLACE alumnosWITH promedio := _prom + 10  
ENDIF 
SKIP 
ENDDO 
BROW 
USE 
IF _prom  = 100  
USE alumnos
GO TOP
DOM WHILE .NOT.EOF() 
IF promedio  < 100  
DELETE 
ENDIF 
SKIP 
ENDDO 
BROW 
USE 
 ENDIF 