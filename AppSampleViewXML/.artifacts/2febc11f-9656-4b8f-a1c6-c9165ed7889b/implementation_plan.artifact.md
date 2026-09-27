# Weather City Information Implementation Plan

Add the city name to the weather dashboard using Android's `Geocoder`.

## Proposed Changes

### UI Components

#### [MODIFY] [activity_main.xml](file:///C:/Users/ramyr/AndroidStudioProjects/AppSampleView/app/src/main/res/layout/activity_main.xml)
- Add a `TextView` for the city name in the weather section.

### Data Layer

#### [MODIFY] [WeatherService.java](file:///C:/Users/ramyr/AndroidStudioProjects/AppSampleView/app/src/main/java/com/ramyres/appsampleview/WeatherService.java)
- Update `WeatherData` to include a `cityName` field.
- In `fetchWeather`, use `Geocoder` to resolve the city name from the latitude and longitude.

### Logic

#### [MODIFY] [MainActivity.java](file:///C:/Users/ramyr/AndroidStudioProjects/AppSampleView/app/src/main/java/com/ramyres/appsampleview/MainActivity.java)
- Update the UI to display the city name returned by `WeatherService`.
- Clean up the location logic to be more robust (using both GPS and Network providers).

## Verification Plan

### Manual Verification
- Deploy to an emulator/device.
- Verify that the city name appears in the weather section.
- Ensure the app handles cases where `Geocoder` cannot find a city name.
