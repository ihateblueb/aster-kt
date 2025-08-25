import Button from "../Button.tsx";
import './Navigation.scss'
import {IconBell, IconHome, IconUserPlus} from "@tabler/icons-react";

function NavigationWidget() {
    return (
        <ul className={`widget-navigation widget padded`}>
            <li>
                <Button wide nav to={'/'}>
                    <IconHome size={18}/>
                    Home
                </Button>
            </li>
            <li>
                <Button wide nav to={'/notifications'}>
                    <IconBell size={18}/>
                    Notifications
                </Button>
            </li>
            <li>
                <Button wide nav to={'/follow-requests'}>
                    <IconUserPlus size={18}/>
                    Follow Requests
                </Button>
            </li>
        </ul>
    )
}

export default NavigationWidget;