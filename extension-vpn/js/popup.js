function fetchIPAddress() {
  fetch('https://api.ipify.org?format=json')
    .then(response => response.json())
    .then(data => {
      const ipAddress = data.ip;
      document.getElementById("ipAddress").innerText = `Your IP: ${ipAddress}`;
    })
    .catch(error => {
      console.error("Error fetching IP address:", error);
    });
}

function toggleProxy(enable) {
  chrome.runtime.sendMessage({ command: "setProxy", enable: enable }, (response) => {
    console.log(response);
    if (response.result === "success") {
      updateVPNStatus(enable);
    }
  });
}

function updateVPNStatus(status) {
  const vpnStatusElement = document.getElementById("vpnStatus");
  vpnStatusElement.innerText = `VPN is ${status ? "ON" : "OFF"}`;
  const button = document.querySelector(".button");
  if (status) {
    button.classList.add("connected");
  } else {
    button.classList.remove("connected");
  }
}

document.getElementById("connectButton").addEventListener("click", () => {
  chrome.runtime.sendMessage({ command: "getProxyStatus" }, (response) => {
    toggleProxy(!response.connected);
  });
});


fetchIPAddress();
chrome.runtime.sendMessage({ command: "getProxyStatus" }, (response) => {
  updateVPNStatus(response.connected);
});
