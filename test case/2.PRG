PUBLIC _nombre
CREATE TABLE alumnos (control N( 5 )  , nombre C( 50 )  , edad N( 5 )  , promedio N( 8,3 )  )
USE alumnos
APPEND BLANK
REPLACE control WITH 101, nombre WITH 'Albert Einstein', edad WITH 78, promedio WITH 100.0
USE alumnos
APPEND BLANK
REPLACE control WITH 401, nombre WITH 'Juan David Guilmour', edad WITH 24, promedio WITH 78.5
USE alumnos
APPEND BLANK
REPLACE control WITH 201, nombre WITH 'Blanca Rojo Pinto', edad WITH 17, promedio WITH 86.3
USE alumnos
GO TOP 
DO WHILE.NOT.EOF() 
IF control  = 101  
STORE nombre TO _nombre
ENDIF 
SKIP 
ENDDO 
BROW 
USE 
? _nombre 
USE alumnos
GO TOP 
DO WHILE.NOT.EOF() 
IF control  = 201  
STORE nombre TO _nombre
ENDIF 
SKIP 
ENDDO 
BROW 
USE 
? _nombre 
USE alumnos
GO TOP 
DO WHILE.NOT.EOF() 
IF control  = 401  
STORE nombre TO _nombre
ENDIF 
SKIP 
ENDDO 
BROW 
USE 
? _nombre 
