console.log("Loaded admin panel script")

function navBackwards() {
    console.log(window.location.pathname)
    if (window.location.href.includes("?since=")) {
        window.history.back()
    }
}