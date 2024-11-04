import {Redirect} from "expo-router";
import 'text-encoding-polyfill';


export default function Index() {

    return <Redirect href="/websockets" />;
}
