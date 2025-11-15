# Gestión de Riego de Plantas (MVP)

**Descripción General**: Una aplicación móvil que ayuda a los usuarios a cuidar de sus plantas permitiéndoles crear una lista personalizada. Cada planta tiene su propia configuración de frecuencia de riego (en días), hora de notificación y la fecha de su último riego. La app muestra el estado de salud de riego (ej. Verde, Naranja, Rojo) y envía recordatorios, permitiendo al usuario marcar la tarea como completada.

## Historias de Usuario (El "Qué")

### Historia 1: Añadir una nueva planta (Prioridad: 1 - MVP)

**Como** un usuario nuevo, **Quiero** añadir una planta a mi lista con su nombre, foto (opcional), frecuencia de riego, hora de recordatorio y **cuándo la regué por última vez**, **Para** que el sistema calcule el próximo riego correctamente.

**Criterios de Aceptación (Cómo sabremos que funciona):**

1. **Dado** que estoy en el formulario de "Añadir Planta", **Cuando** relleno el nombre (obligatorio), la frecuencia (ej. "7" días) y la hora (ej. "09:00"), **Entonces** debo rellenar también "Cuándo se regó por última vez".
2. **Dado** que relleno el campo "Último riego", **Cuando** introduzco "0", **Entonces** el sistema lo interpreta como "Hoy".
3. **Dado** que he rellenado todos los campos obligatorios y pulso "Guardar", **Entonces** la planta se añade a mi lista y su próximo riego se calcula automáticamente (Fecha Último Riego + Frecuencia).

### Historia 2: Marcar una planta como regada (Prioridad: 1 - MVP)

**Como** un usuario, **Quiero** marcar una planta como "regada" fácilmente, **Para** que la app registre la fecha de _hoy_ como el "último riego" y reinicie el contador.

**Criterios de Aceptación:**

1. **Dado** que una planta aparece en mi lista, **Cuando** pulso el botón "Regar" (o similar), **Entonces** la planta se marca visualmente como regada.
2. **Dado** que he marcado una planta como regada, **Cuando** el sistema recalcula sus datos, **Entonces** la "fecha de último riego" de esa planta se actualiza a "Hoy" (0 días).
3. **Dado** que la "fecha de último riego" se ha actualizado, **Entonces** su indicador de estado (ver Historia 5) se actualiza a **Verde** (porque ya no está pendiente).

### Historia 3: Recibir un recordatorio de riego (Prioridad: 1 - MVP)

**Como** un usuario, **Quiero** recibir una notificación push el día que corresponde, **Para** no olvidarme de regar una planta.

**Criterios de Aceptación:**

1. **Dado** que mi "Helecho" se regó por última vez hace 3 días y su frecuencia es de "3 días", y la hora de aviso son las 10:00, **Cuando** llega el día de hoy a las 10:00, **Entonces** recibo una notificación push.
2. **Dado** que una planta tiene un retraso (ej. tocaba ayer y no la regué), **Cuando** llega la hora de notificación hoy, **Entonces** vuelvo a recibir un recordatorio (hasta que la marque).

### Historia 4: Editar/Ver detalles de una planta (Prioridad: 2)

**Como** un usuario, **Quiero** poder ver y editar los detalles de una planta (foto, nombre, frecuencia, hora), **Para** ajustar su cuidado o ver su historial.

**Criterios de Aceptación:**

1. **Dado** que estoy en la pantalla de detalles/edición de una planta, **Cuando** miro su información de riego, **Entonces** veo un texto que dice "Último riego: **Hoy**" (si se regó hace 0 días).
2. **Dado** que estoy en la pantalla de detalles, **Cuando** miro su información de riego, **Entonces** veo "Último riego: **Ayer**" (si se regó hace 1 día).
3. **Dado** que estoy en la pantalla de detalles, **Cuando** miro su información de riego, **Entonces** veo "Último riego: **Hace X días**" (para 2 o más días).

### Historia 5: Visualizar el estado de riego (Prioridad: 1 - MVP)

**Como** un usuario, **Quiero** ver un indicador de color (círculo) junto a cada planta en mi lista, **Para** saber de un vistazo cuáles necesitan atención y su nivel de urgencia.

**Criterios de Aceptación:**

1. **Dado** que una planta está "dentro de plazo" (la fecha de hoy es _anterior_ a su próxima fecha de riego), **Entonces** el indicador es **VERDE**.
2. **Dado** que a una planta le toca regar hoy, o tiene 1 o 2 días de retraso, **Entonces** el indicador es **NARANJA**.
3. **Dado** que una planta tiene 3 o más días de retraso, **Entonces** el indicador es **ROJO**.

---

## Casos Borde y Escenarios de Error

- **¿Qué pasa si el usuario introduce "0" días, un número negativo o letras en la frecuencia?**
    - _Decisión:_ El campo de frecuencia (y el de "último riego hace X días") solo acepta números enteros positivos. El valor **mínimo es 1** (para frecuencia) y **0** (para último riego).
    - _Decisión (Nueva):_ Para evitar errores y siguiendo tu sugerencia de un slider, estableceremos un límite. El valor **máximo será 90 días**. (Suficiente para la mayoría de plantas y evita errores de tecleo como "999").
- **¿Qué pasa si el usuario no da permiso de notificaciones a la app?**
    - _Decisión:_ La app funciona, pero mostrará un aviso no intrusivo en la pantalla principal (ej. "Activa las notificaciones para recibir tus recordatorios") que lleve a la configuración.
- **¿Se puede guardar una planta sin nombre?**
    - _Decisión:_ No. El nombre es obligatorio. La foto es opcional.

## Requisitos Clave (Must-Haves)

- El sistema DEBE permitir una **frecuencia de riego y hora de notificación independiente** para cada planta.
- El usuario DEBE poder añadir una planta indicando **como mínimo un nombre, una frecuencia y cuándo se regó por última vez**.
- El sistema DEBE calcular el próximo riego basado en la fórmula: `Próximo Riego = FechaÚltimoRiego + FrecuenciaEnDías`.
- El sistema DEBE reiniciar el contador **actualizando la "FechaÚltimoRiego" a "Hoy"** cuando el usuario marque la planta como regada.
- El sistema DEBE mostrar un **indicador de estado (Verde/Naranja/Rojo)** basado en la urgencia.
- La frecuencia de riego DEBE ser un entero **entre 1 y 90 días**.
- Los "días desde último riego" DEBE ser un entero **entre 0 y 90 días**.

## Criterios de Éxito (El "Por Qué")

- Sabremos que esto es un éxito si los usuarios marcan como "regada" al menos el 80% de las notificaciones de riego que reciben.
- _Métrica (Nueva):_ Sabremos que el indicador de estado es un éxito si el **porcentaje medio de plantas en estado "Rojo" por usuario disminuye** después del primer mes de uso.
- El objetivo de negocio es **aumentar la retención de usuarios** (que sigan usando la app después de 30 días), demostrando que la app les ayuda a mantener sus plantas sanas.