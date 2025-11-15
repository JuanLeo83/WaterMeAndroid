## 📝 **Notas de Contexto y Desarrollo**
> **Instrucción del desarrollador**: Cualquier decisión importante, tarea no contemplada, aclaración de ambigüedades, o cambio significativo durante el desarrollo debe ser documentado aquí para futuras sesiones.

### **Decisiones de Implementación:**
- *[Pendiente: documentar decisiones tomadas durante desarrollo]*

### **Tareas Adicionales Identificadas:**
- *[Pendiente: añadir tareas no contempladas inicialmente]*

### **Aclaraciones de Ambigüedades:**
- *[Pendiente: documentar resoluciones de puntos ambiguos]*

# Plan: Gestión de Riego de Plantas

**Spec de Referencia**: Spec: Gestión de Riego de Plantas (./spec.md)

## Contexto Técnico (Stack)

- **Lenguaje/Framework**: Kotlin (Android Nativo) con **Jetpack Compose**
- **Base de Datos**: SQLite (Room)
- **Consideraciones**: App local. Foco en alta componentización para futura paridad con SwiftUI.

## Decisiones de Diseño Técnico

1. **Arquitectura General (MVVM)**: Se mantiene. Los `ViewModels` exponen `StateFlows` que serán consumidos por los _Composable functions_ usando `collectAsStateWithLifecycle()`.
2. **Persistencia de Datos (Room)**: Sin cambios. `Plant` (Entity), `PlantDao`, `AppDatabase`.
3. **Lógica de Negocio (Cálculo de Estado)**: Sin cambios. El estado (Verde/Naranja/Rojo) se sigue calculando en el `Repository` o `ViewModel` y se expone a la UI.
4. **Gestión de Notificaciones (WorkManager)**: Sin cambios. Sigue siendo la solución robusta para los recordatorios agendados.
5. **Capa de UI (Jetpack Compose)**: **(DECISIÓN CLAVE)** No se usarán Fragments ni XML. La app tendrá una única `MainActivity` que aloja un `NavHost` de Compose.
6. **Navegación (Compose Navigation)**: Usaremos `androidx.navigation.compose`. Se creará un grafo de navegación (ej. en `/navigation/AppNavigation.kt`) que define las rutas (ej. "list", "detail/{plantId}") y gestiona el paso de argumentos.
7. **Componentización (DECISIÓN CLAVE)**: Siguiendo tu directriz, crearemos un directorio `/ui/components` para "dumb components" (componentes tontos/puros) que sean reutilizables y fáciles de replicar en SwiftUI. Estos componentes solo recibirán estado y emitirán eventos (ej. `onClick`).
    - **Ejemplos**: `WateringStatusIndicator` (el círculo de color), `FrequencySlider` (el slider de 1-90), `PlantListItemCard` (la tarjeta de la lista).
8. **Pantallas (Screens)**: Las "pantallas" serán _Composable functions_ (ej. `PlantListScreen`) que sí son "stateful": se conectan al `ViewModel` para obtener el estado y pasar los lambdas de eventos (ej. `onPlantClicked`) a los componentes "dumb".

## Estructura de Archivos Propuesta (Actualizada para Compose)

/java/com/tuempresa/plantapp
│
├── /data
│   ├── /db
│   │   ├── Plant.kt
│   │   ├── PlantDao.kt
│   │   └── AppDatabase.kt
│   │
│   └── PlantRepository.kt
│
├── /service
│   ├── WateringReminderWorker.kt
│   └── NotificationHelper.kt
│
├── /util
│   ├── WateringStatus.kt
│   ├── DateCalculators.kt
│   └── DateConverters.kt  <-- (Añadido, necesario para Room con LocalDate)
│
├── /ui
│   ├── /components
│   │   ├── WateringStatusIndicator.kt
│   │   ├── FrequencySlider.kt
│   │   ├── PlantListItem.kt
│   │   ├── TimePickerButton.kt
│   │   └── PhotoPicker.kt
│   │
│   ├── /screens
│   │   ├── /list
│   │   │   ├── PlantListScreen.kt
│   │   │   └── PlantListViewModel.kt
│   │   │
│   │   └── /detail
│   │       ├── PlantDetailScreen.kt
│   │       └── PlantDetailViewModel.kt
│   │
│   ├── /navigation
│   │   └── AppNavigation.kt
│   │
│   └── /theme
│       ├── Theme.kt
│       ├── Color.kt
│       └── Typography.kt
│
└── MainActivity.kt