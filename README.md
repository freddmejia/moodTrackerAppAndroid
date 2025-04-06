#  Mood Tracker – Daily Mood & Emotion Journal

**Registro emocional diario moderno con Jetpack Compose + Clean Architecture**

---

##  Descripción

**Mood Tracker** es una aplicación Android construida en un solo día, enfocada en registrar y visualizar el estado emocional del usuario de forma rápida, intuitiva y visualmente atractiva.  
El objetivo es demostrar cómo desarrollar una app funcional y bien estructurada usando **Jetpack Compose**, aplicando buenas prácticas modernas como **Clean Architecture**, **Room**, **Hilt**, y **modularidad de componentes UI**.

---

##  Funcionalidades

-  Registro diario de emociones con emojis, intensidad del sentimiento y nota opcional.  
-  Historial emocional agrupado por día, usando `LazyColumn` y cards limpias.  
-  Pantalla de resumen semanal con gráficas y estadísticas visuales.  
-  Persistencia local con Room y acceso estructurado con `UseCases`.  
-  Inyección de dependencias con Hilt.  
-  UI basada en Jetpack Compose.  
-  Preparada para ProGuard / R8, con reglas de ofuscación y minificación.  

---

##  Tecnologías

| Categoría                | Herramienta / Tecnología     |
|--------------------------|------------------------------|
| Lenguaje                 | Kotlin                       |
| UI                       | Jetpack Compose              |
| Arquitectura             | Clean Architecture (Presentation / Domain / Data) |
| Inyección de dependencias | Hilt                        |
| Persistencia local       | Room + DAO + @Relation       |
| Build Tools              | Gradle + `libs.versions.toml` |
| Seguridad & Optimización | ProGuard / R8                |
