package colas.dichan.ChannelMessaging;

public class PrivateMessage {
    private String userID;
    private int sendbyme;
    private String username;
    private String message;
    private String date;
    private String imageUrl;
    private String everRead;

    public PrivateMessage(String userID, int sendbyme, String username, String message, String date, String imageUrl, String everRead) {
        this.userID = userID;
        this.sendbyme = sendbyme;
        this.username = username;
        this.message = message;
        this.date = date;
        this.imageUrl = imageUrl;
        this.everRead = everRead;
    }

    @Override
    public String toString() {
        return "PrivateMessage{" +
                "userID=" + userID +
                ", sendbyme=" + sendbyme +
                ", username='" + username + '\'' +
                ", message='" + message + '\'' +
                ", date='" + date + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", everRead='" + everRead + '\'' +
                '}';
    }

    public String getUserID() {

        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getSendbyme() {
        return sendbyme;
    }

    public void setSendbyme(int sendbyme) {
        this.sendbyme = sendbyme;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getEverRead() {
        return everRead;
    }

    public void setEverRead(String everRead) {
        this.everRead = everRead;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PrivateMessage that = (PrivateMessage) o;

        if (sendbyme != that.sendbyme) return false;
        if (userID != null ? !userID.equals(that.userID) : that.userID != null) return false;
        if (username != null ? !username.equals(that.username) : that.username != null)
            return false;
        if (message != null ? !message.equals(that.message) : that.message != null) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        if (imageUrl != null ? !imageUrl.equals(that.imageUrl) : that.imageUrl != null)
            return false;
        return everRead != null ? everRead.equals(that.everRead) : that.everRead == null;

    }

}
