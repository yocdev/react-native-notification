import { Platform } from 'react-native'

import RNPushAndroid from './android'
import RNPushIOS from './ios'

const EVENTS = [
  'notification',
  'notificationAuthorization',
  'openNotification',
  'localNotification',
  'register',
  'registrationError',
]

const platPush = Platform.OS === 'ios' ? RNPushIOS : RNPushAndroid

const RNPush = {
  ...platPush,
  addEventListener: function (event, listener) {
    if (!EVENTS.includes(event)) {
      throw 'event not support'
    }
    platPush.addEventListener(event, listener)
  }
}

export default RNPush
