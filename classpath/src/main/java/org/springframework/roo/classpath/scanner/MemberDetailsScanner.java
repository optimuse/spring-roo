package org.springframework.roo.classpath.scanner;

import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetails;
import org.springframework.roo.classpath.itd.ItdTypeDetailsProvidingMetadataItem;
import org.springframework.roo.metadata.MetadataProvider;
import org.springframework.roo.model.CustomDataAccessor;

/**
 * Service that automatically builds a {@link MemberDetails} instance for a given class or interface type. 
 * 
 * <p>
 * Because Spring Roo encourages the use of multiple compilation units (ie AspectJ ITDs) in the creation of
 * a single type, it is relatively complex to build a complete representation of the type. This is especially
 * complex if a representation is desired in the middle of creating an {@link ItdTypeDetailsProvidingMetadataItem}
 * for that same type (as the "rest" of the type information is often needed to produce the new ITD-based metadata,
 * plus there are frequently infinite loops to avoid).
 * 
 * <p>
 * A {@link MemberDetailsScanner} is therefore the recommended way for add-ons to build representations of Java types.
 * There are several reasons:
 * 
 * <ul>
 * <li>The discovery and collation of multiple compilation units is delegated to a specialized, well-defined service
 * ({@link MemberDetailsScanner})</li>
 * <li>A {@link MemberDetailsDecorator} facility permits customization of the visible member information on
 * a per-{@link MetadataProvider} basis, thus offering a way to customize the information seen by a provider</li>
 * <li>{@link MemberDetailsDecorator}s can use the {@link CustomDataAccessor} facility to associate custom information
 * with members and compilation units in an easy manne.</li>
 * </ul>
 * 
 * @author Ben Alex
 * @since 1.1
 *
 */
public interface MemberDetailsScanner {

	/**
	 * Builds {@link MemberDetails} instance for the given {@link ClassOrInterfaceTypeDetails}. In particular,
	 * this includes all ITD members that can be acquired at this time. It also includes all members in the standard Java
	 * source class hierarchy (as per {@link ClassOrInterfaceTypeDetails#getSuperclass()}).
	 * 
	 * <p>
	 * No attempt is made at visibility resolution due to inheritance or aspect overriding, although we may add this in the
	 * future (it is difficult to add definitively given the detected members may be incomplete due to infinite loop avoidance).
	 * 
	 * <p>
	 * An implementation will pass the resulting {@link MemberDetails} through any detected {@link MemberDetailsDecorator}
	 * instances so they can potentially replace the result with a new instance that is ultimately returned. It is required
	 * that implementations invoke decorators in the alphabetic order of each decorator's fully qualified class name.
	 * Only when every {@link MemberDetailsDecorator} returns the same {@link MemberDetails} as it was passed will processing be
	 * regarded as complete. This manages any issues with respect to the order in which {@link MemberDetailsDecorator}s
	 * are invoked.
	 * 
	 * @param metadataProvider the metadata provider requesting the member details (required; may be used for result customization)
	 * @param cid the class or interface to build member information for (required)
	 * @return the discovered member details (never null)
	 */
	public MemberDetails getMemberDetails(MetadataProvider metadataProvider, ClassOrInterfaceTypeDetails cid);

}