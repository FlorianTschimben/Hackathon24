python
import folium
import json
import sys

# Load GeoJSON data from command-line argument
geojson_data = json.loads(sys.argv[1])

# Create the map centered on the first point in the GeoJSON
first_point = geojson_data['features'][0]['geometry']['coordinates']
m = folium.Map(location=[first_point[1], first_point[0]], zoom_start=13)

# Add the GeoJSON layer
folium.GeoJson(geojson_data).add_to(m)

# Save map to HTML file
m.save("C:\\Users\\User\\Info\\20241108 Hackathon 2024\\Hackathon24\\UI\\srcmap.html")
