
import {
  NativeModules,
  Platform,
  PushNotificationIOS,
	NativeAppEventEmitter,
} from 'react-native'

const { RNPush } = NativeModules

let registrationIdIOS = ''

PushNotificationIOS.addEventListener('register', (token) => {
	registrationIdIOS = token
	NativeAppEventEmitter.emit('getRegistrationId', {
    type: 'Apple',
		registrationId: token,
	})
})

const RNPushIOS = {
  getRegistrationInfo: () => {
		return Promise.resolve({
      type: 'Apple',
      registrationId: registrationIdIOS,
    })
  },
	init: () => {
    // have to call this to get fresh registrationId
    PushNotificationIOS.requestPermissions()
  },
	stop: () => {},
	resume: () => {},
	clearAllNotifications: () => {
		PushNotificationIOS.removeAllDeliveredNotifications()
	},
	clearNotificationById: (id) => {
		PushNotificationIOS.removeDeliveredNotifications(id)
	}
}

const M = Platform.OS === 'ios' ? RNPushIOS : RNPush
export default M
