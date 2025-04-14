SmartSense: DHT11 Sensor -> ESP32 CYD -> Raspberry Pi Mosquitto Mqtt Broker & TP Link Router -> Android App


This app allows you to view temperature and humidity values on a gauge or chart. You may also send your own message back to the ESP32 CYD's LCD. Even if you don't have a Raspberry Pi Mosquitto MQTT Broker and a TP Link Router you may use another MQTT Broker (Or even a free public one online like https://www.hivemq.com/demos/websocket-client/).


Smart Sense extracts the surrounding temperature and humidity values from an ESP32 CYD connected to a DHT11 Sensor & sends this data to the Android Studio App through a Raspberry Pi Mosquitto MQTT Broker and a TP Link Router.


How to use it:

1. Download Zip File of this github repository and extract it.
2. Open the application on your android phone/emulator through Android Studio & Open the SmartSenseC++ file in Arduino IDE and run this code through your ESP32 CYD connected to your DHT11 Sensor (Don't forget to configure DHTPIN 27 on line 9).
3. Configure the IP Addresses (line 39-41) in SmartSenseC++ and (line 46) in LauncherActivity after setting up your Raspberry Pi Mosquitto MQTT broker with a TP-Link router.


How to set up your Raspberry Pi as a Mosquitto MQTT broker with a TP-Link router:

⚙️ Step 1: Install Mosquitto MQTT Broker
On the Raspberry Pi:
Open the terminal and run:

bash
Copy
Edit
sudo apt update
sudo apt upgrade -y
sudo apt install mosquitto mosquitto-clients -y
sudo systemctl enable mosquitto
This installs and enables the MQTT broker and command-line clients.

🔧 Step 2: Configure Mosquitto (Optional but Recommended)
Default config is fine for local testing, but for security and flexibility:

Create a config file:

bash
Copy
Edit
sudo nano /etc/mosquitto/mosquitto.conf
Add:

yaml
Copy
Edit
listener 1883
allow_anonymous true
You can change allow_anonymous to false later and set up usernames/passwords for more security.

Restart Mosquitto:

bash
Copy
Edit
sudo systemctl restart mosquitto
🌐 Step 3: Assign a Static IP to Your Pi (via TP-Link Router)
Log into your TP-Link router (usually 192.168.0.1 or 192.168.1.1)

Go to DHCP > Address Reservation

Find your Raspberry Pi's MAC address (from ifconfig or ip a)

Reserve a static IP for it, e.g. 192.168.0.150

Reboot the Pi or reconnect to apply the static IP

🔓 Step 4: Allow MQTT Through the Firewall (if enabled)
If you’re using ufw, allow port 1883:

bash
Copy
Edit
sudo ufw allow 1883
🧪 Step 5: Test MQTT Locally
From your Pi or another device on the same network:

Publish a message:
bash
Copy
Edit
mosquitto_pub -h 192.168.0.150 -t test/topic -m "Hello from Pi"
Subscribe:
bash
Copy
Edit
mosquitto_sub -h 192.168.0.150 -t test/topic
You should see the message pop up!

🔐 (Optional) Step 6: Secure with User Auth or TLS
If you plan to use it beyond local use:

Set allow_anonymous false in config

Add a user:

bash
Copy
Edit
sudo mosquitto_passwd -c /etc/mosquitto/passwd yourusername
Update config:

bash
Copy
Edit
password_file /etc/mosquitto/passwd
Then restart again.

🌍 (Optional) Step 7: Access from Outside Your Network
Set up port forwarding on the TP-Link router:

Forward external port 1883 to your Pi’s IP and port 1883

Use dynamic DNS (like No-IP) if your IP changes often

Make sure to use encryption (TLS) and authentication if exposed to the internet
