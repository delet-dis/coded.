# coded.

<img src="https://user-images.githubusercontent.com/47276603/169459378-a979ce4b-bb21-4a8b-a299-d6d1551eb2d2.png" height = "220" align="right" hspace="50">

[![Android CI](https://github.com/delet-dis/coded./actions/workflows/android.yml/badge.svg)](https://github.com/delet-dis/coded./actions/workflows/android.yml)

[![codebeat badge](https://github.com/delet-dis/coded./actions/workflows/android.yml/badge.svg)](https://github.com/delet-dis/coded./actions/workflows/android.yml)

[![codefactor](https://github.com/delet-dis/coded./actions/workflows/android.yml/badge.svg)](https://github.com/delet-dis/coded./actions/workflows/android.yml)

coded. is an [Android](https://en.wikipedia.org/wiki/Android_(operating_system)) application designed to teach children programming by building programs from blocks of code.

The goal of the project is to make an application that allows a child to build basic algorithms without knowledge of programming languages.

Please check [CONTRIBUTING](CONTRIBUTING.md) page if you want to help.

## Project characteristics and tech-stack

This project takes advantage of best practices, many popular libraries and tools in the Android ecosystem. 

* Tech-stack
    * [100% Kotlin](https://kotlinlang.org/)
    * [Hilt](https://dagger.dev/hilt/) - dependency injection
    * [Firebase](https://firebase.google.com/) - analytics and crash tracking
    * [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) - background operations processing
    * [Flow](https://developer.android.com/kotlin/flow) - data changes observing in business-logic application part
    * [Jetpack](https://developer.android.com/jetpack)
      * [ViewPager2](https://developer.android.com/jetpack/androidx/releases/viewpager2) - displaying Views and Fragments in a swipeable format
      * [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - data changes observing in UI application part
      * [Lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle) - event handling based on lifecycle
      * [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - store and manage UI-related data in a lifecycle conscious way
      * [Android KTX](https://developer.android.com/kotlin/ktx) -  set of Kotlin extensions
      * [Fragment](https://developer.android.com/jetpack/androidx/releases/fragment) -  displaying multiple screens inside activity
      * [ViewBinding](https://developer.android.com/topic/libraries/view-binding) -  getting links to interface elements
* Modern Architecture
  * [Clean architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
  * MVVM
* CI 
    * [GitHub Actions](https://github.com/features/actions)
    * Automatic commits and PR analyzing by 3rd party online tools
* UI
    * [Material design](https://material.io/design)

## Data flows
### 🛬 OnboardingActivity
<img src="https://user-images.githubusercontent.com/47276603/169473214-832f4c65-311d-4287-b5f2-a1f19d82d72a.png" width="600" hspace="5" vspace ="10">

> <img src="https://user-images.githubusercontent.com/47276603/122640649-e3772500-d12a-11eb-98ae-43fc95d000ba.png" width="30" hspace="5"> - request </br>
> <img src="https://user-images.githubusercontent.com/47276603/122640648-e2de8e80-d12a-11eb-869c-e20de1a1f773.png" width="30" hspace="5"> - response


### ✏️ EditorActivity
<img src="https://user-images.githubusercontent.com/47276603/169479030-4291adb8-2421-44c1-95fa-2ba3ebdebc67.png" width="600" hspace="5" vspace ="10">

> <img src="https://user-images.githubusercontent.com/47276603/122640649-e3772500-d12a-11eb-98ae-43fc95d000ba.png" width="30" hspace="5"> - request </br>
> <img src="https://user-images.githubusercontent.com/47276603/122640648-e2de8e80-d12a-11eb-869c-e20de1a1f773.png" width="30" hspace="5"> - response



## Architecture
The entire application follows `clean architecture`.

It contains non-layer components and layers with distinct set of responsibilities.

<img src="https://user-images.githubusercontent.com/47276603/169466815-410c88ba-eded-41da-981e-5a7bab4dd319.png" width="700" hspace="5" vspace ="10">

## More project screenshots
<img src="https://user-images.githubusercontent.com/47276603/169468025-423c7fd6-94b3-4ef8-bcbd-abf7106f12fa.png" width="200" hspace="5" vspace ="10">
<img src="https://user-images.githubusercontent.com/47276603/169467886-3ed0a798-fd6f-4ad7-bf5c-d51b9c11d464.png" width="200" hspace="5" align="left" vspace ="10">
<br/>
<img src="https://user-images.githubusercontent.com/47276603/169468132-54a1b8b3-19f2-4c89-80e1-b4d207cbf6e6.png" width="200" hspace="5" vspace ="10">
<img src="https://user-images.githubusercontent.com/47276603/169468078-344f55e3-5dd6-4f87-b9c4-9b4690a4d2fc.png" width="200" hspace="5" align="left" vspace ="10">
<br/>
<img src="https://user-images.githubusercontent.com/47276603/169468247-42c8c608-40d7-473c-8a5e-0ddcc87f32f2.png" width="200" hspace="5" vspace ="10">
<img src="https://user-images.githubusercontent.com/47276603/169468193-070332d2-93ef-486e-906c-0b9a338f1fae.png" width="200" hspace="5" align="left" vspace ="10">
<br/>
<img src="https://user-images.githubusercontent.com/47276603/169468910-e9a4c293-0f21-4b4c-8ac0-d3f6aff1eec6.png" width="200" hspace="5"  vspace ="10">
<img src="https://user-images.githubusercontent.com/47276603/169468357-d1d0f604-7d71-4d0f-8852-2ed32e7312f9.png" width="200" hspace="5" align="left" vspace ="10">
<br/>
<img src="https://user-images.githubusercontent.com/47276603/169469017-648775ea-dac4-42b6-adf3-a4fa2d5952e8.png" width="200" hspace="5" vspace ="10">
<img src="https://user-images.githubusercontent.com/47276603/169468977-de80a654-e22e-4e9c-951d-375c13cdf68f.png" width="200" hspace="5" align="left" vspace ="10">
<br/>
<img src="https://user-images.githubusercontent.com/47276603/169469153-4f311566-7fd4-4dfa-83e8-aa352f4c2afc.png" width="200" hspace="5" vspace ="10">
<img src="https://user-images.githubusercontent.com/47276603/169469081-97dff306-d7f6-4336-ae46-4ce104f454dc.png" width="200" hspace="5" align="left" vspace ="10">
<br/>
<img src="https://user-images.githubusercontent.com/47276603/169469271-77b7850f-64a9-44f1-822c-fe41cc654ab6.png" width="200" hspace="5" vspace ="10">
<img src="https://user-images.githubusercontent.com/47276603/169469208-3075c72f-0d38-4c17-8a34-d2e9fa77fbbb.png" width="200" hspace="5" align="left" vspace ="10">
<br/>
<img src="https://user-images.githubusercontent.com/47276603/169469388-2157236c-e09c-4d5d-ab12-f72fb0dc8888.png" width="200" hspace="5" vspace ="10">
<img src="https://user-images.githubusercontent.com/47276603/169469345-4ff6bb8b-6b90-4557-84f0-fa1425022517.png" width="200" hspace="5" align="left" vspace ="10">
<br/>
<img src="https://user-images.githubusercontent.com/47276603/169469388-2157236c-e09c-4d5d-ab12-f72fb0dc8888.png" width="200" hspace="5" vspace ="10">
<img src="https://user-images.githubusercontent.com/47276603/169469345-4ff6bb8b-6b90-4557-84f0-fa1425022517.png" width="200" hspace="5" align="left" vspace ="10">
<br/>
<img src="https://user-images.githubusercontent.com/47276603/169469448-8c741390-ed4c-4452-8ec3-b6015619409e.png" width="200" hspace="5" vspace ="10">
<br/>


## License
```
MIT License

Copyright (c) 2022 Igor Efimov

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
