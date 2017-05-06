
import {
  NativeModules,
  Platform,
  PushNotificationIOS,
} from 'react-native'

const { RNPush } = NativeModules

const RNPushIOS = {
  getRegistrationId: () => {
    return RNPush.getRegistrationId()
  }
}

const M = Platform.OS === 'ios' ? RNPushIOS : RNPush
export default M
