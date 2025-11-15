## 📝 **Notas de Contexto y Desarrollo**
> **Instrucción del desarrollador**: 
- Cualquier decisión importante, tarea no contemplada, aclaración de ambigüedades, o cambio significativo durante el desarrollo debe ser documentado aquí para futuras sesiones.
- Cada vez que finalices una tarea debes preguntar si lo que has hecho es correcto e indicar qué se debe probar en caso de ser necesario.
- Cuando el desarrollador diga que todo está correcto, procede a hacer commit de la tarea y continua con la siguiente de la lista.

### **Decisiones de Implementación:**
- **Inyección de Dependencias con Koin** (15/11/2025): Se decidió usar Koin para DI siguiendo principios SOLID. Todos los componentes principales serán inyectados. Se creará `WaterMeApplication.kt` para inicializar Koin y `/di/AppModule.kt` para definir los módulos.
- **Versión de Kotlin 2.1.0** (15/11/2025): Se mantiene Kotlin 2.1.0 en lugar de la más reciente 2.2.21 debido a que KSP aún no tiene una versión estable compatible con Kotlin 2.2.21. La combinación estable es: Kotlin 2.1.0 + KSP 2.1.0-1.0.29 + Room 2.8.3. Se verificó que las versiones KSP 2.2.21-1.0.30 y 2.0.21-1.0.29 no existen en los repositorios.

### **Tareas Adicionales Identificadas:**
- **Corrección Campo "Last Watering"** (15/11/2025): Se identificó que el campo "Last Watering" no debe ser editable en el formulario de añadir nueva planta. En su lugar, las plantas nuevas usan automáticamente "hoy" como fecha de último riego. El campo solo aparecerá como información de solo lectura en el modo de edición futuro.

### **Aclaraciones de Ambigüedades:**
- **Campo "Last Watering" en Añadir vs Editar** (15/11/2025): 
  - **Añadir Nueva Planta**: No se muestra el campo. Se asume automáticamente que se regó "hoy" (LocalDate.now()).
  - **Editar Planta (futuro)**: Se mostrará como label informativo con texto relativo: "Last watering: today", "Last watering: yesterday", "Last watering: 4 days ago", "Last watering: never".

### **Instrucciones de Flujo de Trabajo:**
- **Flujo de desarrollo incremental**: Trabajar tarea por tarea. Al completar cada tarea, esperar confirmación del desarrollador antes de continuar con la siguiente.
- **Gestión de dudas**: Si hay ambigüedades en una tarea, preguntar siempre antes de implementar y documentar la resolución en "Aclaraciones de Ambigüedades".
- **Verificación de tareas completadas**: Revisar el estado actual del proyecto. Si una tarea ya está implementada, marcarla como realizada y continuar con la siguiente.
- **Internacionalización (i18n)**: Todos los textos que se muestren al usuario (interfaz, accesibilidad, notificaciones) deben incluirse en el archivo `strings.xml`. No usar strings hardcodeados en el código. En el futuro se añadirán traducciones a otros idiomas.

### **Guías de Estilo de Código:**

#### **Nomenclatura de Archivos:**
- **Cada pantalla tiene su propia carpeta**: Organizar por feature/pantalla en `/ui/screens/<feature>/`
- **Nomenclatura basada en la feature**: Todos los archivos dentro de la carpeta de una pantalla deben nombrase según la feature, seguido del tipo de archivo.
- **Ejemplos**:
  - `/ui/screens/detail/DetailScreen.kt` - Composable de la pantalla
  - `/ui/screens/detail/DetailViewModel.kt` - ViewModel
  - `/ui/screens/detail/DetailState.kt` - Data class para el estado UI
  - `/ui/screens/detail/DetailIntent.kt` - Sealed class/interface para las intenciones
  - `/ui/screens/list/ListScreen.kt`
  - `/ui/screens/list/ListViewModel.kt`
  - `/ui/screens/list/ListState.kt`

#### **Patrón de Arquitectura de Presentación (MVI - Model-View-Intent):**
- **ViewModel**: Debe exponer una función `handleIntent(intent: <Feature>Intent)` que procese todas las acciones de usuario.
- **Estado Unidireccional**: El ViewModel expone un único `StateFlow<FeatureState>` que representa todo el estado de la pantalla.
- **La UI solo reacciona al estado**: Los Composables observan el StateFlow y se recomponen cuando el estado cambia.
- **Intenciones (Intents)**: Toda acción del usuario se modela como una sealed class/interface que representa la intención (ej. `SavePlant`, `UpdateName`, `SelectPhoto`).
- **Estructura típica**:
  ```kotlin
  // DetailIntent.kt
  sealed interface DetailIntent {
      data class UpdateName(val name: String) : DetailIntent
      data class UpdateFrequency(val days: Int) : DetailIntent
      data object SavePlant : DetailIntent
  }
  
  // DetailState.kt
  data class DetailState(
      val name: String = "",
      val frequency: Int = 7,
      val isLoading: Boolean = false,
      val error: String? = null
  )
  
  // DetailViewModel.kt
  class DetailViewModel : ViewModel() {
      private val _state = MutableStateFlow(DetailState())
      val state: StateFlow<DetailState> = _state.asStateFlow()
      
      fun handleIntent(intent: DetailIntent) {
          when (intent) {
              is DetailIntent.UpdateName -> _state.update { it.copy(name = intent.name) }
              is DetailIntent.SavePlant -> savePlant()
              // ...
          }
      }
  }
  
  // DetailScreen.kt
  @Composable
  fun DetailScreen(viewModel: DetailViewModel = koinViewModel()) {
      val state by viewModel.state.collectAsStateWithLifecycle()
      
      DetailContent(
          state = state,
          onIntent = viewModel::handleIntent
      )
  }
  ```

# Plan: Gestión de Riego de Plantas

**Spec de Referencia**: Spec: Gestión de Riego de Plantas (./spec.md)

## Contexto Técnico (Stack)

- **Lenguaje/Framework**: Kotlin (Android Nativo) con **Jetpack Compose**
- **Base de Datos**: SQLite (Room)
- **Inyección de Dependencias**: Koin
- **Principios de Diseño**: SOLID
- **Consideraciones**: App local. Foco en alta componentización para futura paridad con SwiftUI.

## Decisiones de Diseño Técnico

1. **Arquitectura General (MVVM)**: Se mantiene. Los `ViewModels` exponen `StateFlows` que serán consumidos por los _Composable functions_ usando `collectAsStateWithLifecycle()`.
2. **Inyección de Dependencias (Koin)**: **(DECISIÓN CLAVE)** Se usará Koin para la inyección de dependencias siguiendo principios SOLID. Todos los componentes principales (`Repository`, `Database`, `ViewModels`, `Workers`) serán inyectados. Se creará un módulo de DI en `/di/AppModule.kt` que definirá todos los módulos de Koin.
3. **Persistencia de Datos (Room)**: Sin cambios. `Plant` (Entity), `PlantDao`, `AppDatabase`. La instancia de la base de datos será proporcionada por Koin.
4. **Lógica de Negocio (Cálculo de Estado)**: Sin cambios. El estado (Verde/Naranja/Rojo) se sigue calculando en el `Repository` o `ViewModel` y se expone a la UI.
5. **Gestión de Notificaciones (WorkManager)**: Sin cambios. Sigue siendo la solución robusta para los recordatorios agendados. Las dependencias del Worker serán inyectadas usando Koin.
6. **Capa de UI (Jetpack Compose)**: **(DECISIÓN CLAVE)** No se usarán Fragments ni XML. La app tendrá una única `MainActivity` que aloja un `NavHost` de Compose.
7. **Navegación (Compose Navigation)**: Usaremos `androidx.navigation.compose`. Se creará un grafo de navegación (ej. en `/navigation/AppNavigation.kt`) que define las rutas (ej. "list", "detail/{plantId}") y gestiona el paso de argumentos.
8. **Componentización (DECISIÓN CLAVE)**: Siguiendo tu directriz, crearemos un directorio `/ui/components` para "dumb components" (componentes tontos/puros) que sean reutilizables y fáciles de replicar en SwiftUI. Estos componentes solo recibirán estado y emitirán eventos (ej. `onClick`).
    - **Ejemplos**: `WateringStatusIndicator` (el círculo de color), `FrequencySlider` (el slider de 1-90), `PlantListItemCard` (la tarjeta de la lista).
9. **Pantallas (Screens)**: Las "pantallas" serán _Composable functions_ (ej. `PlantListScreen`) que sí son "stateful": se conectan al `ViewModel` (obtenido vía Koin) para obtener el estado y pasar los lambdas de eventos (ej. `onPlantClicked`) a los componentes "dumb".

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
├── /di
│   └── AppModule.kt  <-- (Añadido, módulos de Koin para DI)
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
│   │   │   ├── ListScreen.kt
│   │   │   ├── ListViewModel.kt
│   │   │   ├── ListState.kt
│   │   │   └── ListIntent.kt
│   │   │
│   │   └── /detail
│   │       ├── DetailScreen.kt
│   │       ├── DetailViewModel.kt
│   │       ├── DetailState.kt
│   │       └── DetailIntent.kt
│   │
│   ├── /navigation
│   │   └── AppNavigation.kt
│   │
│   └── /theme
│       ├── Theme.kt
│       ├── Color.kt
│       └── Typography.kt
│
├── WaterMeApplication.kt  <-- (Añadido, Application class para inicializar Koin)
└── MainActivity.kt