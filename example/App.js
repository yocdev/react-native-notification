import React from 'react'

import {
  View,
  Text,
  Button,
  NativeAppEventEmitter,
} from 'react-native'

import RNPush from 'react-native-notification'

export default class App extends React.Component {
  constructor() {
    super()
    this.state = {
      registrationId: 'loading',
    }
  }

  async componentDidMount() {
    RNPush.init()

    RNPush.getRegistrationId()
      .then((id) => {
        if (id) {
          this.setState({
            registrationId: id,
          })
        }
      })

    NativeAppEventEmitter.addListener('getRegistrationId', (data) => {
      this.setState({
        registrationId: data.registrationId,
      })
    })
	}

  componentWillUnmount() {
    NativeAppEventEmitter.removeAllListeners()
  }

  handleClearPress = async () => {
    await RNPush.clearAllNotifications()
  }

	requestNotificationPermission = () => {
		RNPush.requestPermission()
	}

  render() {
    return (
      <View style={styles.container}>
				<Text selectable={true}>{this.state.registrationId}</Text>
        <View>
          <Button title="Clear All Notification" onPress={this.handleClearPress} />
					<Button title="Request Permission" onPress={this.requestNotificationPermission} />
        </View>
      </View>
    )
  }
}

const styles = {
  container: {
    flex: 1,
    padding: 20,
  },
}
