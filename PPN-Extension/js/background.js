chrome.runtime.onInstalled.addListener(() => {
  console.log("Extension installed");
});
  
let proxyConnected = false;

function setProxy(enable) {
  if (enable) {
    const config = {
      mode: "fixed_servers",
      rules: {
        singleProxy: {
          scheme: "http",
          host: "5.34.194.25",
          port: 3128
        },
        bypassList: ["<local>"]
      }
    };
    chrome.proxy.settings.set(
      { value: config, scope: "regular" },
      () => {
        console.log("Proxy enabled:", config);
      }
    );
    proxyConnected = true
  } else {
    chrome.proxy.settings.clear({ scope: "regular" }, () => {
      console.log("Proxy disabled");
    });
    proxyConnected = false
  }
}

chrome.runtime.onMessage.addListener((request, sender, sendResponse) => {
  if (request.command === "setProxy") {
    setProxy(request.enable);
    sendResponse({ result: "success" });
  } else if (request.command === "getProxyStatus") {
    sendResponse({ connected: proxyConnected });
  }
});
  