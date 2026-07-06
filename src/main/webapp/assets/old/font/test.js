// Import the built-in 'atob' function for decoding base64
function atob(input) {
    return Buffer.from(input, 'base64').toString('binary');
}

// Define your base64 encoded string
const encodedString = "N4IgzgnmAuCmC2IBcoD6YCGA3WBRAdhgEYA2sAJstAE4CusANCKvLPrQcWZUjfU6jYBjAPa18cap1IUqdRswBmI6vAzQAliPzTuc/syLroZAMJiJyAAwCA7hvznx0awNhghGAA6wnlpDbMmDh+LkgATADMAjjUYFr4AJI8ABwALACMKeHhAKwAbAKK1BisYADy+ADK2LDI+QCckblWAOwCRADm8JU1OMighKzIICBMWCIktMNIDYFeGtBCABbIGVbzGPiuIF4iYDsAAkLIkQC+HZ0V1bUDIEN1SKPjk9OP84srO3sHAUzHyDSF2Y9nw5BEtgAKtpHuwSCQOsYyAAhbrIOEI5hYDRCaAqCAAWVhtHhAnIsEUsHURPRJMx6Fq5FRiCQGLsGBIAGsHJ1mbTSSCVOQAOolLzIRQcsAKVCSmAAMRUtgw1B4fBl8DAnUVEgAcqVHiAAOIGnUuASa7XaaBVDQAL0e4RSFq1ZvKtBMDkeaQEwQouBwEgAMiJPJptL9QACkLlgehlhCqnAOdBlibaJ1HuqyRpFJTqGwbcmSKmqj5ZLx5AIAI70GCRkA0DBCTkVtkgWvuMKgDJ3VAaHgZARhnaoEREABWsFxGhwDejDX+GGQAG0ALpxgtgV7h7ZIAC0Q6C0HUtF+R+jzpAhxGJtYqAAinWXMDwn2B8hwsP1KPx1OZ3OdzRhkF7Lkg66bu4O4JMgh6+ie0Bnms/wnEg6z/LeBqPs+IDApE748NEzAjn8zB/tOmiASg16oRkRGHGBEECFu0HaLBR7oAhSFoShaxftemH3k+Xa4UwaQEYC35hIEY6ThRs7uEBtG5Euq4bsxUFTLu7Hwae568WhPoCU8d6wNhInArkEkxlJv5yQBinUcB7TXox6nMCxWkwQeHEwHpRy0YUxnGlhwkwKJID5NZQWoCRMnkQ5860YurlqZB25eWxPm6Yh+k0WsV43iZoU4cCrTWS5sU/qRsn/pRjlRqh4SgWlGkZR63l";  // Replace "your_encoded_string_here" with the actual base64-encoded string
const decodedString = atob(encodedString);
// Decode the base64 string
try {
    // Parse the decoded JSON string
    const jsonData = JSON.parse(decodedString);
    
    
    // Now you can work with the parsed JSON data
    console.log(jsonData);
} catch (error) {
    console.error('Error parsing JSON:', error.message);
}