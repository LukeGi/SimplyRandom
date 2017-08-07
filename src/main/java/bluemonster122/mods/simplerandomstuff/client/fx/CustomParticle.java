package bluemonster122.mods.simplerandomstuff.client.fx;//package bluemonster122.mods.simplethings.client.fx;
//
//import net.minecraft.client.particle.Particle;
//import net.minecraft.entity.Entity;
//import net.minecraft.world.World;
//import net.minecraftforge.fml.client.FMLClientHandler;
//
//public class CustomParticle extends Particle {
//
//
//
//    public CustomParticle(World world, double d, double d1, double d2,  float size, float red, float green, float blue, boolean distanceLimit, boolean depthTest, float maxAgeMul) {
//        super(world, d, d1, d2, 0.0D, 0.0D, 0.0D);
//        particleRed = red;
//        particleGreen = green;
//        particleBlue = blue;
//        particleAlpha = 0.5F; // So MC renders us on the alpha layer, value not actually used
//        particleGravity = 0;
//        motionX = motionY = motionZ = 0;
//        particleScale *= size;
//        moteParticleScale = particleScale;
//        particleMaxAge = (int)(28D / (Math.random() * 0.3D + 0.7D) * maxAgeMul);
//        this.depthTest = depthTest;
//
//        moteHalfLife = particleMaxAge / 2;
//        setSize(0.01F, 0.01F);
//        Entity renderentity = FMLClientHandler.instance().getClient().getRenderViewEntity();
//
//        if(distanceLimit) {
//            int visibleDistance = 50;
//            if (!FMLClientHandler.instance().getClient().gameSettings.fancyGraphics)
//                visibleDistance = 25;
//
//            if (renderentity == null || renderentity.getDistance(posX, posY, posZ) > visibleDistance)
//                particleMaxAge = 0;
//        }
//
//        prevPosX = posX;
//        prevPosY = posY;
//        prevPosZ = posZ;
//    }
//}
