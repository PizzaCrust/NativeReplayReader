package me.tgsc.replay

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

@Serializable data class BattleBus(val AircraftIndex: Int, val Skin: String, val FlightStartLocation: FVector, val FlightStartRotation: FRotator, val FlightSpeed: Float, val TimeTillFlightEnd: Float, val TimeTillDropStart: Float, val TimeTillDropEnd: Float, val ReplicatedFlightTimestamp: Float)
@Serializable data class PlayerElimination(val Eliminated: String, val Eliminator: String, val GunType: Byte, val Time: String, val Knocked: Boolean)
@Serializable data class Stats(val Unknown: Int, val Eliminations: Int, val Accuracy: Float, val Assists: Int, val WeaponDamage: Int, val OtherDamage: Int, val DamageToPlayers: Int, val Revives: Int, val DamageTaken: Int, val DamageToStructures: Int, val MaterialsGathered: Int, val MaterialsUsed: Int, val TotalTraveled: Int)
@Serializable data class TeamStats(val Unknown: Int, val Position: Int, val TotalPlayers: Int)
@Serializable data class Replay(val Eliminations: List<PlayerElimination>, val Stats:
Stats, val TeamStats: TeamStats, val GameData: GameData, val TeamData: List<TeamData>, val PlayerData: List<Player>, val KillFeed: List<KillFeedEntry>, val MapData: MapData) {
    fun getPlayer(id: Int): Player? {
        for (playerDatum in PlayerData) {
            if (playerDatum.Id == id) {
                return playerDatum
            }
        }
        return null
    }

    companion object {
        fun fromJson(str: String) = Json(JsonConfiguration(ignoreUnknownKeys = true, isLenient = true)).parse(serializer(), str)
    }
}
@Serializable data class GameData(val GameSessionId: String, val UtcTimeStartedMatch: String?,
                                  val CurrentPlaylist: String, val AdditionalPlaylistLevels: List<String>, val ActiveGameplayModifiers: List<String>, val MaxPlayers: Int?, val TotalTeams: Int?, val TotalBots: Int?, val TeamSize: Int?, val TotalPlayerStructures: Int?, val IsTournamentRound: Boolean, val TournamentRound: Int?, val IsLargeTeamGame: Boolean?, val AircraftStartTime: Float?, val SafeZonesStartTime: Float?, val WinningTeam: Int?, val WinningPlayerIds: List<Int>)
@Serializable data class InventoryItem(val Count: Int?, val ItemDefinition: String, val OrderIndex: Short?, val Durability: Float?, val Level: Int?, val LoadedAmmo: Int?, val A: Int?, val B: Int?, val C: Int?, val D: Int?)
@Serializable data class KillFeedEntry(val PlayerId: Int?, val PlayerName: String?, val
PlayerIsBot: Boolean, val FinisherOrDowner: Int?, val FinisherOrDownerName: String?, val
FinisherOrDownerIsBot: Boolean, val ReplicatedWorldTimeSeconds: Float?, val Distance: Float?, val
DeathCause: Int?, val DeathLocation: FVector, val DeathCircumstance: Int?, val DeathTags: List<String>?, val IsDowned: Boolean, val IsRevived: Boolean)
@Serializable data class Llama(val Id: Int, val Location: FVector, val HasSpawnedPickups: Boolean, val Looted: Boolean, val LootedTime: Float?, val LandingLocation: FVector)
@Serializable data class MapData(val BattleBusFlightPaths: List<BattleBus>, val SafeZones:
List<SafeZone>, val Llamas: List<Llama>, val SupplyDrops: List<SupplyDrop>, val RebootVans:
List<RebootVan>, val WorldGridStart: FVector2D?, val WorldGridEnd: FVector2D?, val
WorldGridSpacing: FVector2D?, val GridCountX: Int?, val GridCountY: Int?, val WorldGridTotalSize:
FVector2D?)
@Serializable data class ActiveGameplayModifier(val ModifierDef: ItemDefinition)
@Serializable data class Aircraft(val JumpFlashCount: Int, val FlightStartLocation: FVector, val FlightStartRotation: FRotator, val FlightSpeed: Float, val TimeTillFlightEnd: Float, val TimeTillDropStart: Float, val TimeTillDropEnd: Float, val FlightStartTime: Float, val FlightEndTime: Float, val DropStartTime: Float, val DropEndTime: Float, val ReplicatedFlightTimestamp: Float, val AircraftIndex: Int)
@Serializable data class FortClientObservedStat(val StatName: String, val StatValue: Int?)
@Serializable data class FortInventory(val RemoteRole: Int?, val Owner: Int?, val Role: Int?, val Count: Int?, val ItemDefinition: ItemDefinition, val OrderIndex: Short?, val Durability: Float?, val Level: Int?, val LoadedAmmo: Int?, val A: Int?, val B: Int?, val C: Int?, val D: Int?, val InventoryOverflowDate: Boolean?, val bWasGifted: Boolean?, val bIsReplicatedCopy: Boolean?, val bIsDirty: Boolean?, val bUpdateStatsOnCollection: Boolean?, val StateValues: List<FortItemEntryStateValue>, val ParentInventory: Int?, val Handle: Int?, val WrapOverride: Int?, val AlterationInstances: List<DebuggingObject>, val GenericAttributeValues: List<DebuggingObject>, val ReplayPawn: Int?)
@Serializable data class FortItemEntryStateValue(val StateType: Int, val IntValue: Int?, val NameValue: FName)
@Serializable data class GameMemberInfo(val SquadId: Byte, val TeamIndex: Int, val MemberUniqueId: String)
@Serializable data class PlayerPawn(val Owner: NetworkGuid, val bHidden: Boolean?, val bReplicateMovement: Boolean?, val bTearOff: Boolean?, val bCanBeDamaged: Boolean?, val RemoteRole: Int?, val AttachParent: Int?, val LocationOffset: FVector, val RelativeScale3D: FVector, val RotationOffset: FRotator, val AttachSocket: FName, val ExitSocketIndex: Byte?, val AttachComponent: Int?, val Instigator: Int?, val RemoteViewPitch: Byte?, val PlayerState: Int?, val Controller: Int?, val MovementBase: Int?, val BoneName: FName, val Location: FVector, val Rotation: FRotator, val bServerHasBaseComponent: Boolean?, val bRelativeRotation: Boolean?, val bServerHasVelocity: Boolean?, val ReplayLastTransformUpdateTimeStamp: Float?, val ReplicatedMovementMode: Byte?, val bIsCrouched: Boolean?, val bProxyIsJumpForceApplied: Boolean?, val bIsActive: Boolean?, val Position: Float?, val LinearVelocity: FVector, val CurrentMovementStyle: Int?, val bIgnoreNextFallingDamage: Boolean?, val TeleportCounter: Byte?, val PawnUniqueID: Int?, val bIsDying: Boolean?, val CurrentWeapon: Int?, val bIsInvulnerable: Boolean?, val bMovingEmote: Boolean?, val bWeaponActivated: Boolean?, val bIsDBNO: Boolean?, val bWasDBNOOnDeath: Boolean?, val JumpFlashCount: Byte?, val bWeaponHolstered: Boolean?, val FeedbackAudioComponent: Int?, val SpawnImmunityTime: Float?, val JumpFlashCountPacked: Int?, val LandingFlashCountPacked: Int?, val bInterruptCurrentLine: Boolean?, val LastReplicatedEmoteExecuted: Int?, val bCanBeInterrupted: Boolean?, val bCanQue: Boolean?, val ForwardAlpha: Float?, val RightAlpha: Float?, val TurnDelta: Float?, val SteerAlpha: Float?, val GravityScale: Float?, val WorldLookDir: FVector, val bIgnoreForwardInAir: Boolean?, val bIsHonking: Boolean?, val bIsJumping: Boolean?, val bIsSprinting: Boolean?, val Vehicle: Int?, val VehicleApexZ: Float?, val SeatIndex: Byte?, val bIsWaterJump: Boolean?, val bIsWaterSprintBoost: Boolean?, val bIsWaterSprintBoostPending: Boolean?, val BuildingState: Int?, val bIsTargeting: Boolean?, val PawnMontage: Int?, val bPlayBit: Boolean?, val bIsPlayingEmote: Boolean?, val FootstepBankOverride: Int?, val PackedReplicatedSlopeAngles: Short?, val bStartedInteractSearch: Boolean?, val AccelerationPack: Short?, val AccelerationZPack: Byte?, val bIsWaitingForEmoteInteraction: Boolean?, val GroupEmoteLookTarget: ItemDefinition, val bIsSkydiving: Boolean?, val bIsParachuteOpen: Boolean?, val bIsParachuteForcedOpen: Boolean?, val bIsSkydivingFromBus: Boolean?, val bReplicatedIsInSlipperyMovement: Boolean?, val bIsInAnyStorm: Boolean?, val bIsSlopeSliding: Boolean?, val bIsProxySimulationTimedOut: Boolean?, val bIsInsideSafeZone: Boolean?, val bIsOutsideSafeZone: Boolean?, val Zipline: ItemDefinition, val PetState: Int?, val bIsZiplining: Boolean?, val bJumped: Boolean?, val ParachuteAttachment: Int?, val AuthoritativeValue: Int, val RemoteViewData32: Int?, val bNetMovementPrioritized: Boolean?, val EntryTime: Int?, val CapsuleRadiusAthena: Float?, val CapsuleHalfHeightAthena: Float?, val WalkSpeed: Float?, val RunSpeed: Float?, val SprintSpeed: Float?, val CrouchedRunSpeed: Float?, val CrouchedSprintSpeed: Float?, val AnimMontage: Int?, val AnimRootMotionTranslationScale: Int?, val PlayRate: Float?, val BlendTime: Float?, val ForcePlayBit: Boolean?, val IsStopped: Boolean?, val SkipPositionCorrection: Boolean?, val RepAnimMontageStartSection: Int?, val ReplayRepAnimMontageInfo: FGameplayAbilityRepAnimMontage, val RepAnimMontageInfo: FGameplayAbilityRepAnimMontage, val SimulatedProxyGameplayCues: Int?, val WeaponActivated: Boolean?, val bIsInWaterVolume: Boolean?, val BannerIconId: String, val BannerColorId: String, val ItemWraps: List<ItemDefinition>, val SkyDiveContrail: ItemDefinition, val Glider: ItemDefinition, val Pickaxe: ItemDefinition, val bIsDefaultCharacter: Boolean?, val Character: ItemDefinition, val CharacterVariantChannels: List<Int>, val DBNOHoister: NetworkGuid, val DBNOCarryEvent: DebuggingObject, val Backpack: ItemDefinition, val LoadingScreen: ItemDefinition, val Dances: List<ItemDefinition>, val MusicPack: ItemDefinition, val PetSkin: ItemDefinition, val GravityFloorAltitude: Int?, val GravityFloorWidth: Int?, val GravityFloorGravityScalar: Int?, val ReplicatedWaterBody: Int?, val DBNORevivalStacking: Byte?, val ServerWorldTimeRevivalTime: Int?, val FlySpeed: Float?, val NextSectionID: Int?, val bIsCreativeGhostModeActivated: Boolean?, val PlayRespawnFXOnSpawn: Boolean?, val bIsSkydivingFromLaunchPad: Boolean?, val bInGliderRedeploy: Boolean?, val bReplicatedIsInVortex: Boolean?)
@Serializable data class PlaylistInfo(val Id: Int, val Name: String)
@Serializable data class BatchedDamageCues(val HitActor: Int?, val Location: FVector, val Normal: FVector, val Magnitude: Float?, val bWeaponActivate: Boolean?, val bIsFatal: Boolean?, val bIsCritical: Boolean?, val bIsShield: Boolean?, val bIsShieldDestroyed: Boolean?, val bIsShieldApplied: Boolean?, val bIsBallistic: Boolean?, val NonPlayerHitActor: Int?, val NonPlayerLocation: FVector, val NonPlayerNormal: FVector, val NonPlayerMagnitude: Float?, val NonPlayerbIsFatal: Boolean?, val NonPlayerbIsCritical: Boolean?, val bIsValid: Boolean?)
@Serializable data class BroadcastExplosion(val HitActors: List<Int>, val HitResults: List<FHitResult>)
@Serializable data class AddMapMarker(val PlayerID: Int, val InstanceID: Int, val Owner: Int, val WorldPosition: FVector, val WorldPositionOffset: FVector, val WorldNormal: FVector, val ItemDefinition: Int, val ItemCount: Int, val MarkedActor: Int, val bHasCustomDisplayInfo: Boolean, val DisplayName: FText, val Icon: String, val R: Float, val G: Float, val B: Float, val A: Float)
@Serializable data class RemoveMapMarker(val PlayerID: Int, val InstanceID: Int)
@Serializable data class PlayerDamagedResourceBuilding(val BuildingSMActor: Int, val PotentialResourceType: Int, val PotentialResourceCount: Int, val bDestroyed: Boolean, val bJustHitWeakspot: Boolean)
@Serializable data class GameplayCue(val GameplayCueTag: FGameplayTag, val Parameters: FGameplayCueParameters, val PredictionKey: FPredictionKey)
@Serializable data class OnNewLevel(val NewLevel: Int)
@Serializable data class SpawnMachineRepData(val Location: FVector, val SpawnMachineState: Int, val SpawnMachineCooldownStartTime: Float, val SpawnMachineCooldownEndTime: Float, val SpawnMachineRepDataHandle: Int)
@Serializable data class Player(val Id: Int?, val PlayerId: String?, val EpicId: String?, val
PlatformUniqueNetId: String?, val BotId: String?, val IsBot: Boolean?, val PlayerName: String?,
                                val PlayerNameCustomOverride: String?, val StreamerModeName:
                                String?, val Platform: String?, val Level: Int?, val
                                SeasonLevelUIDisplay: Int?, val InventoryId: Int?, val
                                PlayerNumber: Int?, val TeamIndex: Int?, val IsPartyLeader:
                                Boolean, val IsReplayOwner: Boolean, val IsGameSessionOwner:
                                Boolean?, val HasFinishedLoading: Boolean?, val HasStartedPlaying: Boolean?, val HasThankedBusDriver: Boolean?, val IsUsingStreamerMode: Boolean?, val IsUsingAnonymousMode: Boolean?, val Disconnected: Boolean?, val RebootCounter: Int?, val Placement: Int?, val Kills: Int?, val TeamKills: Int?, val DeathCause: Int?, val DeathCircumstance: Int?, val DeathTags: List<String>?, val DeathLocation: FVector?, val DeathTime: Float?, val HasEverSkydivedFromBus: Boolean?, val HasEverSkydivedFromBusAndLanded: Boolean?, val Cosmetics: Cosmetics, val CurrentWeapon: Int?)
@Serializable data class Cosmetics(val CharacterGender: Int?, val CharacterBodyType: Int?, val
Parts: String?, val VariantRequiredCharacterParts: List<String>?, val HeroType: String?, val
BannerIconId: String?, val BannerColorId: String?, val ItemWraps: List<String?>?, val
SkyDiveContrail: String?, val Glider: String?, val Pickaxe: String?, val IsDefaultCharacter:
Boolean?, val Character: String?, val Backpack: String?, val LoadingScreen: String?, val Dances:
List<String>?, val MusicPack: String?, val PetSkin: String?)
@Serializable data class RebootVan(val Id: Int, val Location: FVector, val SpawnMachineState: Int, val SpawnMachineCooldownStartTime: Float, val SpawnMachineCooldownEndTime: Float)
@Serializable data class SafeZone(val Radius: Float, val StartShrinkTime: Float, val FinishShrinkTime: Float, val LastRadius: Float, val LastCenter: FVector, val NextRadius: Float, val NextCenter: FVector, val NextNextRadius: Float, val NextNextCenter: FVector)
@Serializable data class SupplyDrop(val Id: Int, val HasSpawnedPickups: Boolean, val Looted: Boolean, val LootedTime: Float?, val BalloonPopped: Boolean, val BalloonPoppedTime: Float?, val FallSpeed: Float, val LandingLocation: FVector, val FallHeight: Float)
@Serializable data class TeamData(val TeamIndex: Int?, val PlayerIds: List<Int?>, val
PlayerNames: List<String?>, val PartyOwnerId: Int?, val Placement: Int?, val TeamKills: Int?)
@Serializable data class Actor(val ActorNetGUID: NetworkGuid, val Archetype: NetworkGuid, val Level: NetworkGuid, val Location: FVector, val Rotation: FRotator, val Scale: FVector, val Velocity: FVector)
@Serializable data class DebuggingObject(val Bytes: List<Byte>, val TotalBits: Int, val BoolValue: Boolean?, val ShortValue: Int?, val FloatValue: Float?, val IntValue: Int?, val ByteValue: Int?, val IntPacked: Int, val NetId: String, val PossibleExport: List<DebuggingHandle>, val QuantizeVector: FVector, val VectorNormal: FVector, val Vector10: FVector, val Vector100: FVector, val RotatorByte: FRotator, val RotatorShort: FRotator, val AsciiString: String, val UnicodeString: String, val FString: String, val StaticName: String, val DidError: Boolean)
@Serializable data class DebuggingHandle(val NumBits: Int, val Handle: Int)
@Serializable data class EventInfo(val Id: String, val Group: String, val Metadata: String, val StartTime: Int, val EndTime: Int, val SizeInBytes: Int)
@Serializable data class FGameplayAbilityRepAnimMontage(val AnimMontage: NetworkGuid, val PlayRate: Float, val Position: Float, val BlendTime: Float, val NextSectionID: Byte, val bRepPosition: Boolean, val IsStopped: Boolean, val ForcePlayBit: Boolean, val SkipPositionCorrection: Boolean, val bSkipPlayRate: Boolean, val PredictionKey: FPredictionKey, val SectionIdToPlay: Int)
@Serializable data class FGameplayCueParameters(val NormalizedMagnitude: Float, val RawMagnitude: Float, val MatchedTagName: FGameplayTag, val OriginalTag: FGameplayTag, val AggregatedSourceTags: FGameplayTagContainer, val AggregatedTargetTags: FGameplayTagContainer, val Location: FVector, val Normal: FVector, val Instigator: Int, val EffectCauser: Int, val SourceObject: Int, val PhysicalMaterial: Int, val GameplayEffectLevel: Int, val AbilityLevel: Int, val TargetAttachComponent: Int)
@Serializable data class FGameplayTag(val TagName: String, val TagIndex: Int)
@Serializable data class FGameplayTagContainer(val Tags: List<FGameplayTag>)
@Serializable data class FHitResult(val BlockingHit: Boolean, val StartPenetrating: Boolean, val FaceIndex: Int, val Time: Float, val Distance: Float, val Location: FVector, val ImpactPoint: FVector, val Normal: FVector, val ImpactNormal: FVector, val TraceStart: FVector, val TraceEnd: FVector, val PenetrationDepth: Float, val Item: Int, val PhysMaterial: Int, val Actor: Int, val Component: Int, val BoneName: String, val MyBoneName: String)
@Serializable data class FName(val Name: String)
@Serializable data class FPredictionKey(val bIsServerInitiated: Boolean)
@Serializable data class FRotator(val Pitch: Float, val Yaw: Float, val Roll: Float)
@Serializable data class FText(val Namespace: String, val Text: String)
@Serializable data class FVector(val X: Float, val Y: Float, val Z: Float)
@Serializable data class FVector2D(val X: Float, val Y: Float)
@Serializable data class ItemDefinition(val Name: String)
@Serializable data class NetFieldExport(val IsExported: Boolean, val Handle: Int, val CompatibleChecksum: Int, val Name: String, val Type: String, val Incompatible: Boolean)
@Serializable data class NetFieldExportGroup(val PathName: String, val PathNameIndex: Int, val NetFieldExportsLength: Int, val NetFieldExports: List<NetFieldExport>)
@Serializable data class NetworkGuid(val Value: Int)
@Serializable data class ReplayHeader(val NetworkChecksum: Int, val GameNetworkProtocolVersion: Int, val Guid: String, val Major: Short, val Minor: Short, val Patch: Short, val Changelist: Int, val Branch: String, val GameSpecificData: List<String>)
@Serializable data class ReplayInfo(val LengthInMs: Int, val NetworkVersion: Int, val Changelist: Int, val FriendlyName: String, val Timestamp: String, val IsLive: Boolean, val IsCompressed: Boolean, val IsEncrypted: Boolean)