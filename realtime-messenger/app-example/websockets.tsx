import {SafeAreaView, Text} from "react-native";
import WebSocketComponent from "@/components/WebSocketComponent";

export default function WebsocketsScreen() {
    return (
        <SafeAreaView>
            <Text style={{ marginTop: 30 }}>Hello hello!</Text>
            <WebSocketComponent />
        </SafeAreaView>
    )
}
