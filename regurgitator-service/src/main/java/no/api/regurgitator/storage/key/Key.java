package no.api.regurgitator.storage.key;

/**
 *
 */
public interface Key {

    /**
     *
     */
    public String getFullPath();


    /**
     * For getting saving path
     */
    public String getPath();

    /**
     * For getting saving filename
     */
    public String getFileName();

    /**
     *
     */

    public String getKey();

    /**
     *
     */
    public void createKey(String key);

}
