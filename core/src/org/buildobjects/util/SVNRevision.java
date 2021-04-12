package org.buildobjects.util;

/**
 * User: fleipold
 * Date: Oct 22, 2008
 * Time: 12:27:55 PM
 */
public class SVNRevision implements Revision {
    int revisionNumber;

    public SVNRevision(int revisionNumber) {
        this.revisionNumber = revisionNumber;
    }

    // For the benefit of xstream
    public SVNRevision() {
    }

    public int compareTo(Object that) {
        if (that == null){
            throw new NullPointerException("object to compare must not be null.");
        }
        if (!(that instanceof SVNRevision)){
            throw new IllegalArgumentException("Cannot compare SVNRevision and "+that.getClass().getName());
        }

        return new Integer(revisionNumber).compareTo(((SVNRevision)that).revisionNumber);

    }

    public String toString() {
        return "SVN r"+revisionNumber;
    }
}
