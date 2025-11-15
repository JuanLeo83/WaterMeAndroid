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
- [ ] Crear `MainActivity.kt` con `setContent { AppNavigation() }`.
- [ ] Implementar `AppNavigation.kt` en `/navigation/` con rutas `"list"` y `"detail/{plantId}"`.
- [ ] Configurar tema Compose: crear `/ui/theme/Color.kt`, `Typography.kt`, `Theme.kt`.

---

## 🌱 Historia 1: Añadir una nueva planta
**Archivos:**  
`/ui/screens/detail/PlantDetailScreen.kt`  
`/ui/screens/detail/PlantDetailViewModel.kt`  
`/ui/components/PhotoPicker.kt`, `/ui/components/FrequencySlider.kt`, `/ui/components/TimePickerButton.kt`

- [ ] Crear `PlantDetailScreen.kt` (Composable "stateful") con campos:
  - [ ] Nombre (TextField obligatorio)
  - [ ] Foto opcional (`PhotoPicker`)
  - [ ] Frecuencia (`FrequencySlider`)
  - [ ] Hora (`TimePickerButton`)
  - [ ] Último riego (TextField numérico)
  - [ ] Botón “Guardar”
- [ ] Implementar validaciones de entrada (frecuencia 1–90, último riego 0–90).
- [ ] Añadir lógica de "0 = Hoy" para último riego.
- [ ] En `PlantDetailViewModel.kt`:
  - [ ] Crear método `savePlant()` que calcule `nextWateringDate = lastWateringDate + frequency`.
  - [ ] Persistir la nueva planta en `PlantRepository`.
- [ ] Añadir navegación desde `"list"` a `"detail"` al pulsar “Añadir Planta” (`AppNavigation.kt`).
- [ ] Crear composables “dumb”:
  - [ ] `PhotoPicker.kt` (selección de imagen opcional)
  - [ ] `FrequencySlider.kt` (selector 1–90 días)
  - [ ] `TimePickerButton.kt` (selector de hora)

---

## 💧 Historia 2: Marcar una planta como regada
**Archivos:**  
`/ui/screens/list/PlantListScreen.kt`, `/ui/components/PlantListItem.kt`

- [ ] En `PlantListItem.kt`, añadir botón “Regar”.
- [ ] En `PlantListScreen.kt`, manejar `onWatered(plantId)` que actualice `lastWateringDate = hoy`.
- [ ] Actualizar `PlantRepository` y `PlantDao` para persistir el cambio.
- [ ] Emitir nuevo estado vía `PlantListViewModel` (`StateFlow`).
- [ ] Recalcular y actualizar estado de riego (`WateringStatus` → Verde).

---

## 🔔 Historia 3: Recibir un recordatorio de riego
**Archivos:**  
`/service/WateringReminderWorker.kt`, `/service/NotificationHelper.kt`

- [ ] Configurar `WorkManager` para tarea diaria que consulte `PlantRepository`.
- [ ] Implementar lógica: si `today == nextWateringDate`, enviar notificación.
- [ ] Repetir notificación si hay retraso (hasta que se marque como regada).
- [ ] Añadir permisos y canal de notificaciones en `AndroidManifest.xml`.
- [ ] Probar flujo completo: crear planta → esperar día → recibir notificación.

---

## ✏️ Historia 4: Editar/Ver detalles de una planta
**Archivos:**  
`/ui/screens/detail/PlantDetailScreen.kt`, `/ui/screens/detail/PlantDetailViewModel.kt`

- [ ] Reutilizar `PlantDetailScreen.kt` para modo ver/editar con `plantId` recibido.
- [ ] Cargar datos desde `PlantRepository` y rellenar campos.
- [ ] Mostrar texto contextual:
  - [ ] “Último riego: Hoy” (0 días)
  - [ ] “Ayer” (1 día)
  - [ ] “Hace X días” (>1 día)
- [ ] Guardar cambios en planta existente.

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
