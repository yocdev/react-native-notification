
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
		registrationId: token,
	})
})

const RNPushIOS = {
  getRegistrationId: () => {
		if (!registrationIdIOS) {
      PushNotificationIOS.requestPermissions()
		}
		return Promise.resolve(registrationIdIOS)
  },
	requestPermission: () => {
		return PushNotificationIOS.requestPermissions()
	},

	init: () => {},
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
