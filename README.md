<h1 align="center"> Update Checker </h1>
<p align="center">Check if your application is updated based on github releases!</p>

## 🛠️ Get started

### Gradle
```gradle
repositories {
  maven { url 'https://jitpack.io' }
}

dependencies {
  implementation 'com.github.ClarkQuente:Update-Checker:TAG'
}
```
The tag can be found [here](https://jitpack.io/#ClarkQuente/Update-Checker) (tag = version)

## 💻 How Use:

<h3>UpdateChecker constructor:</h3>
- Path = Location where the file will be downloaded. <br/>
- Version = Version which will compare with the latest release tag. <br/>
- User = The github username. <br/>
- Repository = The name of the github repository. <br/>
- DownloadAutomatically = Define if the file will be download if isn't updated. <br/>

<br/>

[Example Code](https://github.com/ClarkQuente/Update-Checker/blob/master/src/main/java/me/clarkquente/updatechecker/ExampleCode.java)

## 🚀 Used technologies

- [GSON](https://github.com/google/gson)

## ⚠️ ATTENTION

Your release must have only a asset (file) to work. Because this lib get the first asset that find.
