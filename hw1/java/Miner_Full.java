import java.util.List;
import processing.core.PImage;
import java.util.Optional;
import java.util.Random;

final class Miner_Full implements Entity
{
   private final EntityKind kind = EntityKind.MINER_FULL;
   private final String id;
   private Point position;
   private final List<PImage> images;
   private int imageIndex;
   private final int resourceLimit;
   private int resourceCount;
   private final int actionPeriod;
   private final int animationPeriod;
/*
   private static final String BLOB_KEY = "blob";
   private static final String BLOB_ID_SUFFIX = " -- blob";
   private static final int BLOB_PERIOD_SCALE = 4;
   private static final int BLOB_ANIMATION_MIN = 50;
   private static final int BLOB_ANIMATION_MAX = 150;

   private static final String ORE_ID_PREFIX = "ore -- ";
   private static final int ORE_CORRUPT_MIN = 20000;
   private static final int ORE_CORRUPT_MAX = 30000;
   private static final int ORE_REACH = 1;

   private static final String QUAKE_KEY = "quake";
   private static final String QUAKE_ID = "quake";
   private static final int QUAKE_ACTION_PERIOD = 1100;
   private static final int QUAKE_ANIMATION_PERIOD = 100;
   private static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;
*/
   private static final String MINER_KEY = "miner";
   private static final int MINER_NUM_PROPERTIES = 7;
   private static final int MINER_ID = 1;
   private static final int MINER_COL = 2;
   private static final int MINER_ROW = 3;
   private static final int MINER_LIMIT = 4;
   private static final int MINER_ACTION_PERIOD = 5;
   private static final int MINER_ANIMATION_PERIOD = 6;
/*
   private static final String OBSTACLE_KEY = "obstacle";
   private static final int OBSTACLE_NUM_PROPERTIES = 4;
   private static final int OBSTACLE_ID = 1;
   private static final int OBSTACLE_COL = 2;
   private static final int OBSTACLE_ROW = 3;

   private static final String ORE_KEY = "ore";
   private static final int ORE_NUM_PROPERTIES = 5;
   private static final int ORE_ID = 1;
   private static final int ORE_COL = 2;
   private static final int ORE_ROW = 3;
   private static final int ORE_ACTION_PERIOD = 4;

   private static final String SMITH_KEY = "blacksmith";
   private static final int SMITH_NUM_PROPERTIES = 4;
   private static final int SMITH_ID = 1;
   private static final int SMITH_COL = 2;
   private static final int SMITH_ROW = 3;

   private static final String VEIN_KEY = "vein";
   private static final int VEIN_NUM_PROPERTIES = 5;
   private static final int VEIN_ID = 1;
   private static final int VEIN_COL = 2;
   private static final int VEIN_ROW = 3;
*/

   public Miner_Full(String id, Point position,
      List<PImage> images, int resourceLimit, int resourceCount,
      int actionPeriod, int animationPeriod)
   {
      // this.kind = kind;
      this.id = id;
      this.position = position;
      this.images = images;
      this.imageIndex = 0;
      this.resourceLimit = resourceLimit;
      this.resourceCount = resourceCount;
      this.actionPeriod = actionPeriod;
      this.animationPeriod = animationPeriod;
   }
   // accessors
   public EntityKind getKind() { return this.kind; }
   public String getID() { return this.id; }
   public Point getPosition() { return this.position; }
   public List<PImage> getImages() { return this.images; }
   public int getImageIndex() { return this.imageIndex; }
   public int getResourceLimit() { return this.resourceLimit; }
   public int getResourceCount() { return this.resourceCount; }
   public int getActionPeriod() { return this.actionPeriod; }

   public void setPosition(Point p) { this.position = p; }
   // Methods

   public Action createActivityAction(WorldModel world,
      ImageStore imageStore)
   {
      return new Action(ActionKind.ACTIVITY, ((Entity)this), world, imageStore, 0);
   }

   public Action createAnimationAction(int repeatCount)
   {
      return new Action(ActionKind.ANIMATION, ((Entity)this), null, null, repeatCount);
   }
/*
   public Point nextPositionOreBlob(WorldModel world,
      Point destPos)
   {
      int horiz = Integer.signum(destPos.getX() - this.position.getX());
      Point newPos = new Point(this.position.getX() + horiz,
         this.position.getY());

      Optional<Entity> occupant = world.getOccupant(newPos);

      if (horiz == 0 ||
         (occupant.isPresent() && !(occupant.get().kind == EntityKind.ORE)))
      {
         int vert = Integer.signum(destPos.getY() - this.position.getY());
         newPos = new Point(this.position.getX(), this.position.getY() + vert);
         occupant = world.getOccupant(newPos);

         if (vert == 0 ||
            (occupant.isPresent() && !(occupant.get().kind == EntityKind.ORE)))
         {
            newPos = this.position;
         }
      }

      return newPos;
   }
*/
   public Point nextPositionMiner(WorldModel world,
      Point destPos)
   {
      int horiz = Integer.signum(destPos.getX() - this.position.getX());
      Point newPos = new Point(this.position.getX() + horiz,
         this.position.getY());

      if (horiz == 0 || world.isOccupied(newPos))
      {
         int vert = Integer.signum(destPos.getY() - this.position.getY());
         newPos = new Point(this.position.getX(),
            this.position.getY() + vert);

         if (vert == 0 || world.isOccupied(newPos))
         {
            newPos = this.position;
         }
      }

      return newPos;
   }
/*
   public static boolean moveToOreBlob(Entity blob, WorldModel world,
      Entity target, EventScheduler scheduler)
   {
      if (Point.adjacent(blob.getPosition(), target.getPosition()))
      {
         world.removeEntity(target);
         scheduler.unscheduleAllEvents(target);
         return true;
      }
      else
      {
         Point nextPos = blob.nextPositionOreBlob(world, target.getPosition());

         if (!blob.getPosition().equals(nextPos))
         {
            Optional<Entity> occupant = world.getOccupant(nextPos);
            if (occupant.isPresent())
            {
               scheduler.unscheduleAllEvents(occupant.get());
            }

            world.moveEntity(blob, nextPos);
         }
         return false;
      }
   }
*/
   public boolean moveToFull(WorldModel world,
      Entity target, EventScheduler scheduler)
   {
      if (Point.adjacent(this.position, target.getPosition()))
      {
         return true;
      }
      else
      {
         Point nextPos = this.nextPositionMiner(world, target.getPosition());

         if (!this.position.equals(nextPos))
         {
            Optional<Entity> occupant = world.getOccupant(nextPos);
            if (occupant.isPresent())
            {
               scheduler.unscheduleAllEvents(occupant.get());
            }

            world.moveEntity(((Entity)this), nextPos);
         }
         return false;
      }
   }
/*
   public static boolean moveToNotFull(Entity miner, WorldModel world,
      Entity target, EventScheduler scheduler)
   {
      if (Point.adjacent(miner.getPosition(), target.getPosition()))
      {
         miner.resourceCount += 1;
         world.removeEntity(target);
         scheduler.unscheduleAllEvents(target);

         return true;
      }
      else
      {
         Point nextPos = miner.nextPositionMiner(world, target.position);

         if (!miner.position.equals(nextPos))
         {
            Optional<Entity> occupant = world.getOccupant(nextPos);
            if (occupant.isPresent())
            {
               scheduler.unscheduleAllEvents(occupant.get());
            }

            world.moveEntity(miner, nextPos);
         }
         return false;
      }
   }
*/
   public void transformFull(WorldModel world,
      EventScheduler scheduler, ImageStore imageStore)
   {
      Entity miner = new Miner_Not_Full(this.id, this.position,
              this.images, this.resourceLimit, 
              0 , this.actionPeriod, 
              this.animationPeriod );

      world.removeEntity(this);
      scheduler.unscheduleAllEvents(this);

      world.addEntity( miner );
      ((Miner_Not_Full)miner).scheduleActions(scheduler, world, imageStore);
   }
/*
   public boolean transformNotFull(WorldModel world,
      EventScheduler scheduler, ImageStore imageStore)
   {
      if (this.resourceCount >= this.resourceLimit)
      {
         Entity miner = Functions.createMinerFull(this.id, this.resourceLimit,
            this.position, this.actionPeriod, this.animationPeriod,
            this.images);

         world.removeEntity(this);
         scheduler.unscheduleAllEvents(this);

         world.addEntity(miner);
         miner.scheduleActions(scheduler, world, imageStore);

         return true;
      }

      return false;
   }
*/
   public int getAnimationPeriod()
   {
      switch (this.kind)
      {
      case MINER_FULL:
      case MINER_NOT_FULL:
      case ORE_BLOB:
      case QUAKE:
         return this.animationPeriod;
      default:
         throw new UnsupportedOperationException(
            String.format("getAnimationPeriod not supported for %s",
            this.kind));
      }
   }

   public void nextImage()
   {
      this.imageIndex = (this.imageIndex + 1) % this.images.size();
   }

   public void executeMinerFullActivity(WorldModel world,
      ImageStore imageStore, EventScheduler scheduler)
   {
      Optional<Entity> fullTarget = world.findNearest(this.position,
         EntityKind.BLACKSMITH);

      if (fullTarget.isPresent() &&
         this.moveToFull(world, fullTarget.get(), scheduler))
      {
         this.transformFull(world, scheduler, imageStore);
      }
      else
      {
          scheduler.scheduleEvent(this,
            this.createActivityAction(world, imageStore),
            this.actionPeriod);
      }
   }
/*
   public void executeMinerNotFullActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
   {
      Optional<Entity> notFullTarget = world.findNearest(this.position,
         EntityKind.ORE);

      if (!notFullTarget.isPresent() ||
         !Entity.moveToNotFull(this, world, notFullTarget.get(), scheduler) ||
         !this.transformNotFull(world, scheduler, imageStore))
      {
         scheduler.scheduleEvent(this,
            this.createActivityAction(world, imageStore),
            this.actionPeriod);
      }
   }

   public void executeOreActivity(WorldModel world,
      ImageStore imageStore, EventScheduler scheduler)
   {
      Point pos = this.position;  // store current position before removing

      world.removeEntity(this);
      scheduler.unscheduleAllEvents(this);

      Entity blob = Functions.createOreBlob(this.id + BLOB_ID_SUFFIX,
         pos, this.actionPeriod / BLOB_PERIOD_SCALE,
         BLOB_ANIMATION_MIN +
            Functions.rand.nextInt(BLOB_ANIMATION_MAX - BLOB_ANIMATION_MIN),
         imageStore.getImageList(BLOB_KEY));

      world.addEntity(blob);
      blob.scheduleActions(scheduler, world, imageStore);
   }

   public void executeOreBlobActivity(WorldModel world,
      ImageStore imageStore, EventScheduler scheduler)
   {
      Optional<Entity> blobTarget = world.findNearest(this.position, EntityKind.VEIN);
      long nextPeriod = this.actionPeriod;

      if (blobTarget.isPresent())
      {
         Point tgtPos = blobTarget.get().position;

         if (Entity.moveToOreBlob(this, world, blobTarget.get(), scheduler))
         {
            Entity quake = Functions.createQuake(tgtPos,
               imageStore.getImageList(QUAKE_KEY));

            world.addEntity(quake);
            nextPeriod += this.actionPeriod;
            quake.scheduleActions(scheduler, world, imageStore);
         }
      }

      scheduler.scheduleEvent(this,
         this.createActivityAction(world, imageStore),
         nextPeriod);
   }

   public void executeQuakeActivity(WorldModel world,
      ImageStore imageStore, EventScheduler scheduler)
   {
      scheduler.unscheduleAllEvents(this);
      world.removeEntity(this);
   }

   public void executeVeinActivity(WorldModel world,
      ImageStore imageStore, EventScheduler scheduler)
   {
      Optional<Point> openPt = world.findOpenAround(this.position);

      if (openPt.isPresent())
      {
         Entity ore = Functions.createOre(ORE_ID_PREFIX + this.id,
            openPt.get(), ORE_CORRUPT_MIN +
               Functions.rand.nextInt(ORE_CORRUPT_MAX - ORE_CORRUPT_MIN),
            imageStore.getImageList(ORE_KEY));
         world.addEntity(ore);
         ore.scheduleActions(scheduler, world, imageStore);
      }

      scheduler.scheduleEvent(this,
         this.createActivityAction(world, imageStore),
         this.actionPeriod);
   }
*/
   public void scheduleActions(EventScheduler scheduler,
      WorldModel world, ImageStore imageStore)
   {
      switch (this.kind)
      {
      case MINER_FULL:
         scheduler.scheduleEvent(this,
            this.createActivityAction(world, imageStore),
            this.actionPeriod);
         scheduler.scheduleEvent(this, this.createAnimationAction(0),
            this.getAnimationPeriod());
         break;
/*
      case MINER_NOT_FULL:
         scheduler.scheduleEvent(this,
            this.createActivityAction(world, imageStore),
            this.actionPeriod);
         scheduler.scheduleEvent(this,
            this.createAnimationAction(0), this.getAnimationPeriod());
         break;

      case ORE:
         scheduler.scheduleEvent(this,
            this.createActivityAction(world, imageStore),
            this.actionPeriod);
         break;

      case ORE_BLOB:
         scheduler.scheduleEvent(this,
            this.createActivityAction(world, imageStore),
            this.actionPeriod);
         scheduler.scheduleEvent(this,
            this.createAnimationAction(0), this.getAnimationPeriod());
         break;

      case QUAKE:
         scheduler.scheduleEvent(this,
            this.createActivityAction(world, imageStore),
            this.actionPeriod);
         scheduler.scheduleEvent(this,
            this.createAnimationAction(QUAKE_ANIMATION_REPEAT_COUNT),
            this.getAnimationPeriod());
         break;

      case VEIN:
         scheduler.scheduleEvent(this,
            this.createActivityAction(world, imageStore),
            this.actionPeriod);
         break;
*/
      default:
      }
   }
}
