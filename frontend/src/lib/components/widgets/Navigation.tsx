import Button from "../Button.tsx";
import './Navigation.scss'
import {
    IconBell,
    IconBookmark,
    IconDashboard,
    IconDots,
    IconFolder,
    IconHash,
    IconHome,
    IconSearch,
    IconSettings,
    IconUserPlus
} from "@tabler/icons-react";
import {useRouterState} from "@tanstack/react-router";

function NavigationWidget() {
    const pathname = useRouterState().location.pathname;

    return (
        <ul className={`widget-navigation widget padded`}>
            <li>
                <Button wide nav primary={pathname === "/"} to={'/'}>
                    <IconHome size={18}/>
                    Home
                </Button>
            </li>
            <li>
                <Button wide nav primary={pathname === "/notifications"} to={'/notifications'}>
                    <IconBell size={18}/>
                    Notifications
                </Button>
            </li>
            <li>
                <Button wide nav primary={pathname === "/explore"} to={'/explore'}>
                    <IconHash size={18}/>
                    Explore
                </Button>
            </li>
            <li>
                <Button wide nav primary={pathname === "/follow-requests"} to={'/follow-requests'}>
                    <IconUserPlus size={18}/>
                    Follow Requests
                </Button>
            </li>
            <br/>
            <li>
                <Button wide nav primary={pathname === "/search"} to={'/search'}>
                    <IconSearch size={18}/>
                    Search
                </Button>
            </li>
            <li>
                <Button wide nav primary={pathname === "/bookmarks"} to={'/bookmarks'}>
                    <IconBookmark size={18}/>
                    Bookmarks
                </Button>
            </li>
            <li>
                <Button wide nav primary={pathname === "/drive"} to={'/drive'}>
                    <IconFolder size={18}/>
                    Drive
                </Button>
            </li>
            <br/>
            <li>
                <Button wide nav>
                    <IconDots size={18}/>
                    More
                </Button>
            </li>
            <li>
                <Button wide nav primary={pathname === "/admin"} to={'/admin'}>
                    <IconDashboard size={18}/>
                    Dashboard
                </Button>
            </li>
            <li>
                <Button wide nav primary={pathname === "/settings"} to={'/settings'}>
                    <IconSettings size={18}/>
                    Settings
                </Button>
            </li>
        </ul>
    )
}

export default NavigationWidget;