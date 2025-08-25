import './Sidebar.scss'
import NavigationWidget from "./widgets/Navigation.tsx";

function Sidebar({left, right}: { left?: boolean, right?: boolean }) {
    function render() {
        if (left) {
            return (
                <>
                    <NavigationWidget/>
                </>
            )
        } else if (right) {
            return (
                <>
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