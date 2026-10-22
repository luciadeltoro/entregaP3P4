# entregaP3P4

En esta práctica he hecho una aplicación web para ver los materiales del aula maker y enviar una solicitud de préstamo. He usado Spring Boot, Spring MVC y Thymeleaf. Los datos del catálogo están en un array y las solicitudes no se guardan.

## Cómo ejecutar el proyecto

Hace falta tener Java 21. Desde esta carpeta se puede arrancar la aplicación con:

```bash
./mvnw spring-boot:run
```

Después hay que abrir [http://localhost:8080](http://localhost:8080) en el navegador.

Para ejecutar la prueba automática:

```bash
./mvnw test
```

## Variables y datos que se muestran en las páginas

- Las propiedades de `SolicitudPrestamoForm` son los campos del formulario. Spring rellena el objeto con los datos enviados y después se validan.
- Los parámetros se reciben al entrar en los métodos del controlador. Por ejemplo, `codigo` viene de la ruta de la ficha y `turno` de la dirección web. Al enviar el formulario, el controlador recibe el DTO y `BindingResult`, que contiene los errores.
- Las variables locales, como `recogida`, `devolucion`, `dias` y `diasUnidad`, se usan dentro del método donde se declaran.
- Los datos que añado al `Model` se pueden usar en la plantilla que devuelve el método. Por ejemplo, `materiales` se usa para recorrer el catálogo y crear las opciones del selector. `solicitud` contiene los datos del formulario y `dias` y `diasUnidad` se muestran en el resumen. Si hay errores, se vuelven a mostrar el formulario, los valores que se pudieron enlazar y sus mensajes.

El catálogo está guardado en memoria como un array `String[][]`. Al enviar una solicitud válida solo se muestra el resumen; no se guarda ni se confirma una reserva.

## Comprobaciones

### Prueba automática

He ejecutado `./mvnw test` y ha terminado correctamente (`BUILD SUCCESS`), que comprueba que la aplicación carga.

### Pruebas manuales

También he iniciado la aplicación y he probado estas direcciones y envíos:

| Prueba | Resultado |
|---|---|
| Abrir `/` | Se muestra el catálogo con los tres materiales y sus enlaces. |
| Abrir `/materiales/MICRO?turno=tarde` | Se muestra la ficha del microcontrolador y el turno «tarde». |
| Abrir `/materiales/NO-EXISTE` | Se devuelve un 404 y se muestra la página de error correspondiente. |
| Enviar una solicitud válida | Se muestra el resumen con los días del préstamo y el total de días-unidad. |
| Escribir letras en unidades y una fecha con formato incorrecto | Se vuelve al formulario con mensajes comprensibles y no se muestra el resumen. |
