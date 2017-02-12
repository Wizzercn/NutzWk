package cn.wizzer.framework.shiro.remember;

import org.apache.shiro.io.SerializationException;
import org.apache.shiro.io.Serializer;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;

import java.io.*;
import java.util.Collection;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Creates A GZIPed rememberMe cookie, based on the patch for SHIRO-226 (https://issues.apache.org/jira/browse/SHIRO-226)
 * Created by wizzer on 2017/1/18.
 */

public class SimplePrincipalSerializer implements Serializer<PrincipalCollection> {
    /**
     * Magic number to signal that this is a SimplePrincipalSerializer file so that we don't try to decode something crap.
     */
    private static final int MAGIC = 0x0BADBEEF;

    public byte[] serialize(PrincipalCollection pc) throws SerializationException {
        ByteArrayOutputStream ba = new ByteArrayOutputStream();

        try {
            GZIPOutputStream gout = new GZIPOutputStream(ba);
            ObjectOutputStream out = new ObjectOutputStream(gout);

            // Write the magic number which allows us to decode it later on
            out.writeInt(MAGIC);

            // Limited to 32768 realms. Should be enough for everybody.
            out.writeShort(pc.getRealmNames().size());

            for (String realm : pc.getRealmNames()) {
                out.writeUTF(realm);

                Collection<?> principals = pc.fromRealm(realm);

                // Again, limited to 32768 principals.
                out.writeShort(principals.size());

                for (Object principal : principals) {
                    out.writeObject(principal);
                }
            }
            gout.finish();
        } catch (IOException e) {
            throw new SerializationException(e.getMessage());
        }
        return ba.toByteArray();
    }

    public PrincipalCollection deserialize(byte[] serialized) throws SerializationException {
        ByteArrayInputStream ba = new ByteArrayInputStream(serialized);

        try {
            GZIPInputStream gin = new GZIPInputStream(ba);
            ObjectInputStream in = new ObjectInputStream(gin);
            SimplePrincipalCollection pc = new SimplePrincipalCollection();

            // Check magic number
            if (in.readInt() != MAGIC)
                throw new SerializationException(
                        "Not valid magic number while deserializing stored PrincipalCollection - possibly obsolete cookie.");

            int numRealms = in.readShort();

            // realms loop
            for (int i = 0; i < numRealms; i++) {
                String realmName = in.readUTF();

                int numPrincipals = in.readShort();

                // principals loop
                for (int j = 0; j < numPrincipals; j++) {
                    Object principal = in.readObject();

                    pc.add(principal, realmName);
                }
            }

            return pc;
        } catch (IOException e) {
            throw new SerializationException(e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new SerializationException(e.getMessage());
        }
    }
}