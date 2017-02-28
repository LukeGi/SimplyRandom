package bluemonster122.mods.simplethings.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageParticle implements IMessage {
    private EnumParticleTypes particleType;
    private double xCoord;
    private double yCoord;
    private double zCoord;
    private double xSpeed;
    private double ySpeed;
    private double zSpeed;

    public MessageParticle() {
    }

    public MessageParticle(EnumParticleTypes particleType, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed) {
        this.particleType = particleType;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.zCoord = zCoord;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.zSpeed = zSpeed;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        particleType = EnumParticleTypes.getParticleFromId(buf.readInt());
        xCoord = buf.readDouble();
        yCoord = buf.readDouble();
        zCoord = buf.readDouble();
        xSpeed = buf.readDouble();
        ySpeed = buf.readDouble();
        zSpeed = buf.readDouble();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(particleType.ordinal());
        buf.writeDouble(xCoord);
        buf.writeDouble(yCoord);
        buf.writeDouble(zCoord);
        buf.writeDouble(xSpeed);
        buf.writeDouble(ySpeed);
        buf.writeDouble(zSpeed);
    }

    @SuppressWarnings("MethodCallSideOnly")
    public static class MessageHandler implements IMessageHandler<MessageParticle, MessageParticle> {
        @Override
        public MessageParticle onMessage(MessageParticle message, MessageContext ctx) {
            Minecraft.getMinecraft().player.getEntityWorld().spawnParticle(message.particleType, message.xCoord,
                    message.yCoord, message.zCoord,
                    message.xSpeed, message.ySpeed,
                    message.zSpeed
            );
            return null;
        }
    }
}
