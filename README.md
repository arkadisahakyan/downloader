# File Downloader
This will help you download a file from the Internet simply

## Usage
```java
// The file url (resource)
URL file = new URL("https://www.wikipedia.org/portal/wikipedia.org/assets/img/Wikipedia-logo-v2.png");
// Get a connection to the resource
URLConnection connection = file.openConnection();
// Allocate memory for the resource
byte[] buffer = new byte[connection.getContentLength()];

// Create a task and execute (download). The file data will be stored in the buffer
FileDownloader downloadTask = new FileDownloader(buffer, connection);
Thread t = new Thread(downloadTask);
t.start();

// Wait for the download to complete
t.join();
System.out.println("Downloaded successfully!");

// Convert and save data to a local file
FileOutputStream output = new FileOutputStream("Wikipedia-logo-v2.png");
output.write(buffer);
```
