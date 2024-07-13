
<p align=center>
<img src="https://github.com/user-attachments/assets/648a7e1c-e5e6-4fb3-b098-f1f5b90fd9fe" width=400/>
</p>


# PPN (Your One and Only VPN)
PPN is basically a v2ray VPN client, which you can use to connect either to your personal v2ray servers, or the ones that are defined in this project.

PPN has three main parts:
- `Android App`
- `Website`
- `Chrome Extension`

## Android App
The android app is created using `java` and `golang`, and has two main modules as below:
- `App`: It containts the main structure of the program and its designs. 
- `V2ray`: It has the main API and libraries, that we've used to link our app to the v2ray client.

#### Running the App
You just need to open its directory with `Android Studio` and run it on whatever (any Android Device of course ;D) you want.

## Website
The website is created using `React + Vite` and it's basically just a template. You can further modify the Website to your needs.

#### Running the Webstire
Make sure that you have `npm` installed on your device. (You can reach to this [link](https://docs.npmjs.com/downloading-and-installing-node-js-and-npm) for further information on how to install it.)
After that just move into the websites directory and then you can run it using the commands below.
- For installing the dependencies:
```
npm install
```
- For running the Website:
```
npm run dev
```

## Chrome Extension
The extension is also pretty simple. You just have to create a `manifest.json` file, then modify it to your needs and at last you can add your `html`, `css` and `js` files.

[Here](https://www.freecodecamp.org/news/building-chrome-extension/) is a straight forward tutorial on how to create your first chrome extension.

#### Running the Extension
First open your Chrome. Then head to the `Manage Extensions` tab where you can see and manage all of your extensions. On the top left corner there is a button that says `Load unpacked`. 
Click on that and then select the extensions directory. Now you can use your newly added chrome extension :D.
