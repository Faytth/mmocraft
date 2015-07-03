package org.unallied.mmocraft.skills;

import org.unallied.mmocraft.tools.input.SeekableLittleEndianAccessor;
import org.unallied.mmocraft.tools.output.GenericLittleEndianWriter;

/**
 * A container for all of the skills that the player has, such as Constitution / Strength.
 * @author Alexandria
 *
 */
public class Skills {
    /** All of the player's skills. */
    protected long[] skills = new long[SkillType.values().length];
    
    /** You shouldn't increase this past 400 or an overflow can occur.  */
    private static final int MAX_LEVEL = 150;
    
    /** The total experience needed to reach the next level. */
    private static final long[] experienceChart = new long[MAX_LEVEL];
    
    /**
     * Creates a Skills object, which is used for storing all of the experience for a player's skills.
     */
    public Skills() {
        init();
    }
    
    private void init() {
        // Initialize the experience chart.  NOTE:  Our formula has rounding errors.  Does that matter?
        for (int i=0; i < MAX_LEVEL; ++i) {
            int level = i + 1;
            experienceChart[i] = (long)Math.floor(Math.pow(2, level/7.5) * 500 + ((level - 1) * (level / 2.0) * level * 5) - 548);
        }
    }
    
    /**
     * Retrieves the player's skill experience.  Skills are in the same order as SkillType.
     * @return skills
     */
    public long[] getSkills() {
        return skills;
    }
    
    /**
     * Sets the experience for a single skill.  Negative values count as 0.
     * @param type The skill type to assign the experience for.
     * @param exp The new experience.
     * @return returns true if the skill leveled up or down, else false
     */
    public boolean setExperience(SkillType type, long exp) {
        if (type == null) { // Guard
            return false;
        }
        int oldLevel = 0;
        int newLevel = 0;
        
        synchronized (skills) {
            exp = exp < 0 ? 0 : exp;
            oldLevel = getLevel(type);
            skills[type.getValue()] = exp;
            newLevel = getLevel(type);
        }
        
        return newLevel != oldLevel;
    }
    
    /**
     * Adds an amount of experience to a single skill.  If adding the experience
     * would result in an overflow, the experience is set to the maximum amount.
     * Negative values are ignored.
     * @param type
     * @param exp
     * @return returns true if the skill leveled up or down, else false
     */
    public boolean addExperience(SkillType type, long exp) {
        if (type == null || exp <= 0) { // Guard
            return false;
        }
        int oldLevel = 0;
        int newLevel = 0;
        
        synchronized (skills) {
            oldLevel = getLevel(type);
            if (skills[type.getValue()] + exp > 0) {
                skills[type.getValue()] += exp;
            } else {
                skills[type.getValue()] = Long.MAX_VALUE;
            }
            newLevel = getLevel(type);
        }
        
        return newLevel != oldLevel;
    }
    
    /**
     * Retrieves the level of a skill.  If the type is null, then a value of 0
     * is returned.
     * @param type The type of the skill to retrieve the level for.
     * @return level
     */
    public int getLevel(SkillType type) {
        if (type == null) {
            return 0;
        }
        return getLevel(skills[type.getValue()]);
    }
    
    /**
     * Retrieves the level of a skill based on the experience.
     * TODO:  This should use a binary search algorithm.
     * @param experience The current experience in the skill.
     * @return level
     */
    public int getLevel(long experience) {
        if (experience < 0) { // Guard
            return 0;
        }
        int result = 1;
        for (int i=0; i < MAX_LEVEL - 1 && experienceChart[i + 1] <= experience; ++i, ++result);
        return result;
    }

    public byte[] getBytes() {
        GenericLittleEndianWriter writer = new GenericLittleEndianWriter();
        
        writer.writeInt(skills.length);
        for (long skill : skills) {
            writer.writeLong(skill);
        }

        return writer.toByteArray();
    }
    
    /**
     * Retrieves this class from an SLEA, which contains the raw bytes of this class
     * obtained from the getBytes() method.
     * @param slea A seekable little endian accessor that is currently at the position containing
     *             the bytes of a Skills object.
     * @return skills
     */
    public static Skills fromBytes(SeekableLittleEndianAccessor slea) {
        Skills result = new Skills();
        
        int numberOfSkills = slea.readInt();
        for (int i=0; i < result.skills.length && i < numberOfSkills; ++i) {
            result.skills[i] = slea.readLong();
        }
        
        return result;
    }


    /**
     * Retrieves the experience of the skill type.  This is the total amount of experience
     * for this skill type.  Returns 0 if there's a problem.
     * @param type The skill type
     * @return totalExperience
     */
    public long getTotalExperience(SkillType type) {
        if (type == null) {
            return 0;
        }
        return getTotalExperience(type.getValue());
    }
    
    /**
     * Retrieves the experience of the skill type.  This is the total amount of experience
     * for this skill type.  Returns 0 if there's a problem.
     * @param type The skill type
     * @return totalExperience
     */
    public long getTotalExperience(int type) {
        if (type < 0 || type >= skills.length) {
            return 0;
        }
        return skills[type];
    }
    
    /**
     * Retrieves the experience towards the current level.  Returns 0 if there's
     * a problem.
     * @param type The skill type
     * @return currentExperience
     */
    public long getExperience(SkillType type) {
        if (type == null) {
            return 0;
        }
        return getExperience(type.getValue());
    }
    
    /**
     * Retrieves the experience towards the current level.  Returns 0 if there's
     * a problem.
     * @param type The skill type
     * @return currentExperience
     */
    public long getExperience(int type) {
        if (type < 0 || type >= skills.length) {
            return 0;
        }
        int level = getLevel(skills[type]);
        return skills[type] - experienceChart[level-1];
    }

    /**
     * Retrieves the amount of experience needed to go from the last level to
     * the next level.  If the player is the max level, returns 0.
     * @param type The skill to retrieve the level experience for.
     * @return levelExperience
     */
	public long getLevelExperience(SkillType type) {
		if (type == null) {
			return 0;
		}
		return getLevelExperience(type.getValue());
	}

    /**
     * Retrieves the amount of experience needed to go from the last level to
     * the next level.  If the player is the max level, returns 0.
     * @param type The skill id to retrieve the level experience for.
     * @return levelExperience
     */
	public long getLevelExperience(int type) {
		if (type < 0 || type >= skills.length) {
            return 0;
        }
        int level = getLevel(skills[type]);
        return level >= experienceChart.length ? 0: experienceChart[level] - experienceChart[level-1];
	}
}
