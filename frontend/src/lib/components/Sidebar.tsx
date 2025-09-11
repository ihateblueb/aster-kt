import './Sidebar.scss'
import NavigationWidget from "./widgets/Navigation.tsx";
import AccountWidget from "./widgets/Account.tsx";
import ComposeWidget from "./widgets/Compose.tsx";

function Sidebar({left, right}: { left?: boolean, right?: boolean }) {
    function render() {
        if (left) {
            return (
                <>
                    <NavigationWidget/>
                    <AccountWidget/>
                </>
            )
        } else if (right) {
            return (
                <>
                    <ComposeWidget/>
                </>
            )
        }
    }

    return (
        <div className={`sidebar${left ? " left" : (right ? " right" : "")}`}>
            {render()}
        </div>
    )
}

export default Sidebar;