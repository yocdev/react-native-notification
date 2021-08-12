import {
  NativeModules,
  Platform,
	NativeAppEventEmitter,
} from 'react-native'

// import PushNotificationIOS from '@react-native-community/push-notification-ios'

const {RNCNotification} = NativeModules

// let registrationIdIOS = ''
// PushNotificationIOS.addEventListener('register', (token) => {
// 	registrationIdIOS = token
// })
const notificationEmitter = new NativeAppEventEmitter(RNCNotification)

const RNPush = {
  addEventListener: (event, handler) => {
    notificationEmitter.add(event,handler)
    // if (event === 'register') {
    //   return PushNotificationIOS.addEventListener('register', (token) => {
	  //     registrationIdIOS = token
    //     handler({
    //       type: 'Apple',
    //       registrationId: token,
    //     })
    //   })
    // } else {
    //   PushNotificationIOS.addEventListener(event, handler)
    // }
  },
  getRegistrationId: (code) => {
		// return Promise.resolve({
    //   type: 'Apple',
    //   registrationId: registrationIdIOS,
    // })
    return RNCNotification.getRegistrationId(code)
  },
  clearBadge: () => {
    // PushNotificationIOS.setApplicationIconBadgeNumber(0)
  },
	init: () => {
    // have to call this to get fresh registrationId
    // PushNotificationIOS.requestPermissions()
  },
	stop: () => {},
	resume: () => {},
	removeAllNotifications: () => {
		// PushNotificationIOS.removeAllDeliveredNotifications()
	},
	removeNotifications: (id) => {
		// PushNotificationIOS.removeDeliveredNotifications(id)
	}
}

export default RNPush
