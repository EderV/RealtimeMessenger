import React, {useEffect, useRef, useState} from 'react';
import {Animated, Button, ScrollView, StyleSheet, Text, TextInput, View} from 'react-native';
import SockJS from 'sockjs-client';
import {Client, IMessage, StompConfig} from '@stomp/stompjs';
import Uuid from "expo-modules-core/src/uuid";
import delay = Animated.delay;

interface ChatMessage {
    uuid: string,
    sender: string,
    recipient: string,
    content: string,
    status: string,
    dateTime: number,
}

export default function WebSocketComponent() {
    const [connectionStatus, setConnectionStatus] = useState<string>('Connecting...');
    const [message, setMessage] = useState<string>('');
    const [messagesReceived, setMessagesReceived] = useState<string[]>([]);

    const client = useRef<Client | null>(null);

    useEffect(() => {
        const stompConfig: StompConfig = {
            connectHeaders: {
                // login: "User1_+97459304928",
                login: "User2_+97459977345",
                passcode: "password"
            },
            // brokerURL: 'ws://192.168.1.26:15674/ws',
            webSocketFactory: () => new SockJS("http://192.168.1.26:8080/ws"),
            debug: function (str: string) {
                // if (!(str.includes("PING") || str.includes("PONG"))) {
                //     console.log('STOMP: ' + str);
                // }
            },
            reconnectDelay: 3000,
            onConnect: function (frame) {
                console.log("connected")
                console.log(frame)
                setConnectionStatus("Connected")

                // client.current?.subscribe('/queue/User1_+97459304928', (msg: IMessage) => {
                client.current?.subscribe('/queue/User2_+97459977345', (msg: IMessage) => {
                    console.log('Message received in queue: ' + msg.body);
                }, {
                    id: 'sub-002',
                    ack: 'auto',
                });

                // client.current?.subscribe('/queue/ack.User1_+97459304928', (msg: IMessage) => {
                client.current?.subscribe('/queue/ack.User2_+97459977345', (msg: IMessage) => {
                    console.log('Message received in ack: ' + msg.body);
                }, {
                    id: 'sub-003',
                    ack: 'auto',
                });

                delay(100)
                sendReadyMessage()
            },
            onDisconnect: function (frame: any) {
                console.log("disconnected")
                setConnectionStatus("Disconnected")
            },
            onWebSocketClose: function (event: any) {
                setConnectionStatus("Closed")
                console.error(event)
            },
            onStompError: (frame: any) => {
                setConnectionStatus("Error")
                console.error('Additional details: ' + frame);
            },
        }
        client.current = new Client(stompConfig);

        client.current?.activate()

        // Clean up function
        return () => {
            client.current?.deactivate();
        };
    }, []);

    const sendMessage = () => {
        if (client.current && client.current.connected) {
            const chatMessage: ChatMessage = {
                uuid: Uuid.v4(),
                sender: 'User1_+97459304928',
                recipient: 'User2_+97459977345',
                content: message,
                status: 'NONE',
                dateTime: Date.now(),
            };

            client.current.publish({
                destination: '/app/chat',
                body: JSON.stringify(chatMessage),
            });

            setMessage(''); // Clear the input field
        } else {
            console.log('STOMP client is not connected');
        }
    };

    const sendReadyMessage = () => {
        if (client.current && client.current.connected) {
            client.current.publish({
                destination: '/app/chat/ready',
                body: "",
            });
        } else {
            console.log('STOMP client is not connected');
        }
    };

    return (
        <View style={styles.container}>
            <Text style={styles.status}>Status: {connectionStatus}</Text>
            <ScrollView style={styles.messagesContainer}>
                {messagesReceived.map((msg, index) => (
                    <Text key={index} style={styles.message}>
                        {msg}
                    </Text>
                ))}
            </ScrollView>
            <View style={styles.inputContainer}>
                <TextInput
                    style={styles.input}
                    value={message}
                    onChangeText={setMessage}
                    placeholder="Enter message"
                />
                <Button title="Send" onPress={sendMessage} />
            </View>
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        padding: 16,
        borderColor: "#000000",
        borderWidth: 4,
    },
    status: {
        marginBottom: 16,
        fontSize: 16,
        fontWeight: 'bold',
        color: '#000000',
    },
    messagesContainer: {
        marginBottom: 16,
    },
    message: {
        padding: 8,
        borderBottomColor: '#ccc',
        borderBottomWidth: 1,
    },
    inputContainer: {
        flexDirection: 'row',
        alignItems: 'center',
    },
    input: {
        borderColor: '#ccc',
        borderWidth: 1,
        padding: 8,
        marginRight: 8,
    },
});
