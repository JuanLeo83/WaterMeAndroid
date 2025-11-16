# ✅ Lista de Tareas — Gestión de Riego de Plantas (Compose)

---

## 🧩 Setup (Infraestructura y Configuración)
- [x] Crear estructura de carpetas base según `/java/com/juanleodev/waterme`.
- [x] Configurar **Koin** (Inyección de Dependencias):
  - [x] Añadir dependencias de Koin en `build.gradle.kts`.
  - [x] Crear `WaterMeApplication.kt` para inicializar Koin.
  - [x] Crear `/di/AppModule.kt` con módulos básicos (Database, Repository).
  - [x] Registrar `WaterMeApplication` en `AndroidManifest.xml`.
- [x] Configurar `Room` (SQLite): revisar o crear `AppDatabase.kt`, `PlantDao.kt`, `Plant.kt`, `PlantRepository.kt`.
- [x] Configurar `WorkManager` y `NotificationHelper.kt` para recordatorios.
- [x] Crear `MainActivity.kt` con `setContent { AppNavigation() }`.
- [x] Implementar `AppNavigation.kt` en `/navigation/` con rutas `"list"` y `"detail/{plantId}"`.
- [x] Configurar tema Compose: verificar `/ui/theme/Color.kt`, `Type.kt`, `Theme.kt`.

---

## 🌱 Historia 1: Añadir una nueva planta ✅ COMPLETADA
**Archivos:**  
`/ui/screens/detail/PlantDetailScreen.kt`  
`/ui/screens/detail/PlantDetailViewModel.kt`  
`/ui/components/PhotoPicker.kt`, `/ui/components/FrequencySlider.kt`, `/ui/components/TimePickerButton.kt`

- [x] Crear `PlantDetailScreen.kt` (Composable "stateful") con campos:
  - [x] Nombre (TextField obligatorio)
  - [x] Foto opcional (`PhotoPicker`)
  - [x] Frecuencia (`FrequencySlider`)
  - [x] Hora (`TimePickerButton`)
  - [x] Último riego (TextField numérico)
  - [x] Botón "Guardar"
- [x] Implementar validaciones de entrada (frecuencia 1–90, último riego 0–90).
- [x] Añadir lógica de "0 = Hoy" para último riego.
- [x] En `PlantDetailViewModel.kt`:
  - [x] Crear método `savePlant()` que calcule `nextWateringDate = lastWateringDate + frequency`.
  - [x] Persistir la nueva planta en `PlantRepository`.
- [x] Añadir navegación desde `"list"` a `"detail"` al pulsar "Añadir Planta" (`AppNavigation.kt`).
- [x] Crear composables "dumb":
  - [x] `PhotoPicker.kt` (selección de imagen opcional)
  - [x] `FrequencySlider.kt` (selector 1–90 días)
  - [x] `TimePickerButton.kt` (selector de hora)

---

## 💧 Historia 2: Marcar una planta como regada ✅ COMPLETADA
**Archivos:**  
`/ui/screens/list/PlantListScreen.kt`, `/ui/components/PlantListItem.kt`

- [x] En `PlantListItem.kt`, añadir botón "Regar".
- [x] En `PlantListScreen.kt`, manejar `onWatered(plantId)` que actualice `lastWateringDate = hoy`.
- [x] Actualizar `PlantRepository` y `PlantDao` para persistir el cambio.
- [x] Emitir nuevo estado vía `PlantListViewModel` (`StateFlow`).
- [x] Recalcular y actualizar estado de riego (`WateringStatus` → Verde).

---

## 🔔 Historia 3: Recibir un recordatorio de riego ✅ COMPLETADA
**Archivos:**  
`/service/WateringReminderWorker.kt`, `/service/NotificationHelper.kt`, `/service/WaterPlantReceiver.kt`

- [x] Configurar `WorkManager` para tarea diaria que consulte `PlantRepository`.
- [x] Implementar lógica: si `today == nextWateringDate`, enviar notificación.
- [x] Repetir notificación si hay retraso (hasta que se marque como regada).
- [x] Añadir permisos y canal de notificaciones en `AndroidManifest.xml`.
- [x] Probar flujo completo: crear planta → esperar día → recibir notificación.

---

## ✏️ Historia 4: Editar/Ver detalles de una planta
**Archivos:**  
`/ui/screens/detail/PlantDetailScreen.kt`, `/ui/screens/detail/PlantDetailViewModel.kt`

- [x] Reutilizar `PlantDetailScreen.kt` para modo ver/editar con `plantId` recibido.
- [x] Cargar datos desde `PlantRepository` y rellenar campos.
- [x] Mostrar texto contextual:
  - [x] "Último riego: Hoy" (0 días)
  - [x] "Ayer" (1 día)
  - [x] "Hace X días" (>1 día)
- [x] Guardar cambios en planta existente.

---

## 🌈 Historia 5: Visualizar el estado de riego
**Archivos:**  
`/ui/components/WateringStatusIndicator.kt`, `/util/WateringStatus.kt`

- [ ] Crear `WateringStatusIndicator.kt` (círculo verde/naranja/rojo).
- [ ] En `WateringStatus.kt`, definir `getStatus(lastWatering, frequency)`:
  - [ ] Verde: dentro del plazo
  - [ ] Naranja: hoy o retraso 1–2 días
  - [ ] Rojo: retraso ≥3 días
- [ ] Integrar en `PlantListItem.kt` (mostrar indicador junto al nombre).
- [ ] Asegurar cambio dinámico del color al marcar como regada.

---

## ⚙️ Extras (Cross-cutting / UX)
- [ ] Mostrar aviso en `PlantListScreen.kt` si no hay permisos de notificación (abrir configuración).
- [ ] Añadir `EmptyState` composable si no hay plantas registradas.
- [ ] Implementar `AppTheme` global con colores consistentes (`Theme.kt`).

---
