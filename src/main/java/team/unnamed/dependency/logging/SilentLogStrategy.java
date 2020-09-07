package team.unnamed.dependency.logging;

/**
 * Does nothing with the messages
 */
class SilentLogStrategy implements LogStrategy {

    // lazy-initialized singleton instance because
    // this strategy doesn't require external instances
    static final LogStrategy INSTANCE = new SilentLogStrategy();

    private SilentLogStrategy() {
    }

    @Override
    public void info(String message) {
    }

    @Override
    public void warning(String message, Throwable... errors) {
    }

    @Override
    public void error(String message, Throwable... errors) {
    }

}
