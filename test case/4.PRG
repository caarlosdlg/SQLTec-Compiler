
 declare @edad int

 assign @edad := 25

 case
 when @edad = 0 then print 'La edad es cero'
when @edad < 18 then print 'Eres un nino aun'
when @edad >= 18 and @edad < 60 then
 print 'Ya eres adulto'
when @edad >= 60 then print 'Eres de la 3a edad'
 end

 while @edad <= 30
 begin
 print @edad
 assign @edad := @edad + 1
end
end
