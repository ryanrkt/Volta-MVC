package volta.core;

import java.util.Objects;
import volta.enums.MethodHttp;

public class UrlMethodeHttpMapping {
    private String url;
    private MethodHttp methode;

    public UrlMethodeHttpMapping(String url,MethodHttp methodHttp){
        this.url=url;
        this.methode= methodHttp;
    }

    public UrlMethodeHttpMapping(){
        
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public MethodHttp getMethode() {
        return methode;
    }
    public void setMethode(MethodHttp methode) {
        this.methode = methode;
    }

    @Override 
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UrlMethodeHttpMapping m = (UrlMethodeHttpMapping) o;
        
        return Objects.equals(this.url, m.url) && this.methode == m.methode;
    } 

    @Override
    public int hashCode() {
        return Objects.hash(url, methode);
    }
}