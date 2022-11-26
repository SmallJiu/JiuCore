package cat.jiu.core.types;

/**
 * {@code SUCCESS}: All done<p>
 * {@code UNABLE_WRITE}: success, but not write to storage file<p>
 * {@code NULL}: UUID is null<p>
 * {@code ZERO}: After subtract it will be negative number<p>
 * {@code NOT_FOUND_VALUE:} Not found of value name<p>
 * {@code FILE_NOT_FOUND}: Not found values storage file<p>
 * {@code FAIL}: unknown Error<p>
 * {@code IOError}: IOException<p>
 * {@code NOT_ARRIVAL_TIME}: The number of changes did not reach the specified number<p>
 * {@code Initialization}: name can NOT be Initialization
 * 
 * @author small_jiu
 */
public enum ValueStateType {
	/** All done */ SUCCESS,
	/** success, but not write to storage file */ UNABLE_WRITE,
	/** UUID is null */ NULL,
	/** After subtract, it will be negative number */ ZERO,
	/** Not found of value name */ NOT_FOUND_VALUE,
	/** Not found value's storage file */ FILE_NOT_FOUND,
	/** unknown Error */ FAIL,
	/** The number of changes did not reach the specified number */NOT_ARRIVAL_TIME,
	/** IOException */ IOError,
	/** name can NOT be Initialization*/ Initialization;
}
