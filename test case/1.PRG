PUBLIC _color
PUBLIC _velocidad
STORE 'Verde'  TO _color
STORE 200  TO _velocidad
IF _velocidad  > 100  
STORE 'Rojo'  TO _color
 ENDIF 
CREATE TABLE infracciones (id N( 5 )  , placas C( 10 )  , importe N( 8,3 )  )
USE infracciones
APPEND BLANK
REPLACE id WITH 101, placas WITH 'EGV-041', importe WITH 1803.20
DELETE FILE infracciones.DBF