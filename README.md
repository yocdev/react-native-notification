
# react-native-notification

## Getting started

`$ npm install react-native-notification --save`

### Mostly automatic installation

`$ react-native link react-native-notification`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-notification` and add `RNPush.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNPush.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import me.youchai.rnpush.RNPushPackage;` to the imports at the top of the file
  - Add `new RNPushPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-notification'
  	project(':react-native-notification').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-notification/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-notification')
  	```


## Usage
```javascript
import RNPush from 'react-native-notification';

// TODO: What to do with the module?
RNPush;
```
